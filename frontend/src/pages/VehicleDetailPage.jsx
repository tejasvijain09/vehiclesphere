import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import { 
  Fuel, 
  Gauge, 
  Users, 
  Calendar, 
  MapPin, 
  CheckCircle, 
  AlertCircle,
  ArrowLeft,
  Sparkles,
  Crown,
  Star,
  Heart,
  MessageSquare,
  ShieldCheck
} from 'lucide-react';

export default function VehicleDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();

  const [vehicle, setVehicle] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Reviews & Wishlist State
  const [reviews, setReviews] = useState([]);
  const [ratingSummary, setRatingSummary] = useState(null);
  const [isWishlisted, setIsWishlisted] = useState(false);
  const [wishlistLoading, setWishlistLoading] = useState(false);

  // Booking Form State
  const today = new Date().toISOString().split('T')[0];
  const tomorrow = new Date(Date.now() + 86400000).toISOString().split('T')[0];
  
  const [pickupDate, setPickupDate] = useState(today);
  const [returnDate, setReturnDate] = useState(tomorrow);
  const [pickupLocation, setPickupLocation] = useState('');
  const [notes, setNotes] = useState('');
  
  // Real-time calculation state
  const [calculation, setCalculation] = useState(null);
  const [calcLoading, setCalcLoading] = useState(false);
  const [bookingLoading, setBookingLoading] = useState(false);
  const [bookingSuccess, setBookingSuccess] = useState(null);
  const [bookingError, setBookingError] = useState(null);

  useEffect(() => {
    const fetchVehicle = async () => {
      try {
        const res = await api.get(`/vehicles/${id}`);
        setVehicle(res.data);
        setPickupLocation(res.data.location || '');
      } catch (err) {
        setError('Vehicle not found or failed to load.');
      } finally {
        setLoading(false);
      }
    };

    const fetchReviews = async () => {
      try {
        const [revRes, sumRes] = await Promise.all([
          api.get(`/reviews/vehicle/${id}`),
          api.get(`/reviews/vehicle/${id}/summary`),
        ]);
        setReviews(revRes.data);
        setRatingSummary(sumRes.data);
      } catch (err) {
        console.warn('Failed to load reviews', err);
      }
    };

    fetchVehicle();
    fetchReviews();

    if (user) {
      api.get(`/wishlist/check/${id}`)
        .then((res) => setIsWishlisted(res.data))
        .catch(() => {});
    }
  }, [id, user]);

  const handleToggleWishlist = async () => {
    if (!user) {
      navigate('/login', { state: { from: `/vehicles/${id}` } });
      return;
    }
    setWishlistLoading(true);
    try {
      if (isWishlisted) {
        await api.delete(`/wishlist/${id}`);
        setIsWishlisted(false);
      } else {
        await api.post(`/wishlist/${id}`);
        setIsWishlisted(true);
      }
    } catch (err) {
      console.error('Wishlist error', err);
    } finally {
      setWishlistLoading(false);
    }
  };

  // Recalculate price whenever dates change
  useEffect(() => {
    if (!vehicle || !pickupDate || !returnDate) return;

    if (new Date(returnDate) <= new Date(pickupDate)) {
      setCalculation(null);
      return;
    }

    const calcPrice = async () => {
      setCalcLoading(true);
      try {
        const res = await api.get(
          `/bookings/calculate-price?vehicleId=${vehicle.id}&pickupDate=${pickupDate}&returnDate=${returnDate}`
        );
        setCalculation(res.data);
        setBookingError(null);
      } catch (err) {
        setCalculation(null);
      } finally {
        setCalcLoading(false);
      }
    };

    calcPrice();
  }, [vehicle, pickupDate, returnDate]);

  const handleBookingSubmit = async (e) => {
    e.preventDefault();
    setBookingError(null);

    if (!user) {
      navigate('/login', { state: { from: `/vehicles/${id}` } });
      return;
    }

    if (!pickupDate || !returnDate) {
      setBookingError('Please choose both pickup and return dates.');
      return;
    }

    if (new Date(returnDate) <= new Date(pickupDate)) {
      setBookingError('Return date must be at least 1 day after pickup date.');
      return;
    }

    setBookingLoading(true);
    try {
      const res = await api.post('/bookings', {
        vehicleId: vehicle.id,
        pickupDate,
        returnDate,
        pickupLocation: pickupLocation.trim(),
        notes: notes.trim(),
      });
      setBookingSuccess(res.data);
    } catch (err) {
      setBookingError(err.response?.data?.message || 'Failed to submit booking request.');
    } finally {
      setBookingLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-16 text-center">
        <div className="w-10 h-10 border-4 border-blue-600 border-t-transparent rounded-full animate-spin mx-auto"></div>
        <p className="text-sm text-slate-500 mt-3">Loading vehicle details...</p>
      </div>
    );
  }

  if (error || !vehicle) {
    return (
      <div className="max-w-3xl mx-auto px-4 py-16 text-center">
        <AlertCircle className="w-12 h-12 text-rose-500 mx-auto mb-3" />
        <h2 className="text-2xl font-bold text-slate-800">Vehicle Not Found</h2>
        <p className="text-sm text-slate-500 mt-2">{error}</p>
        <Link to="/vehicles" className="inline-block mt-6 px-4 py-2 bg-blue-600 text-white rounded-xl text-sm font-bold">
          Back to Vehicles
        </Link>
      </div>
    );
  }

  const formattedPrice = Number(vehicle.pricePerDay).toLocaleString('en-IN');

  return (
    <div className="w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8 overflow-x-hidden">
      
      {/* Breadcrumb */}
      <div className="flex items-center gap-2 text-xs font-semibold text-slate-500 flex-wrap">
        <Link to="/vehicles" className="hover:text-blue-600 flex items-center gap-1">
          <ArrowLeft className="w-3.5 h-3.5" />
          <span>Fleet Catalog</span>
        </Link>
        <span>/</span>
        <span className="text-slate-800">{vehicle.brand}</span>
        <span>/</span>
        <span className="text-blue-600 font-bold">{vehicle.name}</span>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 lg:gap-10">
        
        {/* Left Column: Vehicle Visuals & Specs */}
        <div className="lg:col-span-2 space-y-6 sm:space-y-8">
          
          {/* Main Large Image */}
          <div className="relative rounded-3xl overflow-hidden bg-slate-100 border border-slate-200/80 shadow-md">
            <img
              src={vehicle.imageUrl}
              alt={vehicle.name}
              className="w-full h-72 sm:h-96 md:h-[450px] object-cover"
              onError={(e) => {
                e.target.src = 'https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=1200&q=80';
              }}
            />
            <div className="absolute top-4 left-4 flex gap-2">
              <span className="px-3 py-1.5 rounded-xl bg-white/95 backdrop-blur text-xs font-extrabold text-slate-900 shadow-sm">
                {vehicle.categoryName}
              </span>
              <span className={`px-3 py-1.5 rounded-xl text-xs font-extrabold shadow-sm ${
                vehicle.available ? 'bg-emerald-600 text-white' : 'bg-rose-600 text-white'
              }`}>
                {vehicle.available ? 'Available' : 'Booked'}
              </span>
            </div>
          </div>

          {/* Vehicle Title & Brand & Wishlist */}
          <div className="space-y-2">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2 text-xs font-bold text-blue-600 uppercase tracking-widest">
                <span>{vehicle.brand}</span>
                <span>•</span>
                <span>Model Year {vehicle.modelYear}</span>
              </div>
              <button
                type="button"
                onClick={handleToggleWishlist}
                disabled={wishlistLoading}
                className={`flex items-center gap-1.5 px-3 py-1.5 rounded-xl border text-xs font-bold transition shadow-sm ${
                  isWishlisted
                    ? 'bg-rose-50 border-rose-200 text-rose-600'
                    : 'bg-white border-slate-200 text-slate-700 hover:border-rose-200 hover:text-rose-600'
                }`}
              >
                <Heart className={`w-3.5 h-3.5 ${isWishlisted ? 'fill-current' : ''}`} />
                <span>{isWishlisted ? 'Saved in Wishlist' : 'Save to Wishlist'}</span>
              </button>
            </div>

            <h1 className="text-2xl sm:text-4xl font-extrabold text-slate-900 tracking-tight">
              {vehicle.name}
            </h1>

            {/* Ratings & Station info */}
            <div className="flex flex-wrap items-center gap-3 text-xs text-slate-500 pt-1">
              {ratingSummary && ratingSummary.totalReviews > 0 ? (
                <div className="flex items-center gap-1.5 px-2.5 py-1 bg-amber-50 border border-amber-200 rounded-lg text-amber-800 font-bold">
                  <Star className="w-3.5 h-3.5 fill-amber-500 text-amber-500" />
                  <span>{ratingSummary.averageRating.toFixed(1)}</span>
                  <span className="text-amber-600/70 font-normal">({ratingSummary.totalReviews} reviews)</span>
                </div>
              ) : (
                <div className="flex items-center gap-1.5 px-2.5 py-1 bg-slate-50 border border-slate-200 rounded-lg text-slate-600 font-medium">
                  <Star className="w-3.5 h-3.5 text-slate-400" />
                  <span>New to Fleet (No reviews yet)</span>
                </div>
              )}

              <span className="flex items-center gap-1">
                <MapPin className="w-4 h-4 text-blue-500" />
                <span>Station: {vehicle.location}</span>
              </span>
              <span>•</span>
              <span className="font-mono bg-slate-100 px-2 py-0.5 rounded text-slate-700 font-semibold">
                Reg: {vehicle.registrationNumber}
              </span>
            </div>
          </div>

          {/* Specification Badges */}
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 sm:gap-4 p-5 sm:p-6 bg-white rounded-2xl border border-slate-200/80 shadow-sm">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-blue-50 flex items-center justify-center text-blue-600 shrink-0">
                <Fuel className="w-5 h-5" />
              </div>
              <div>
                <p className="text-[10px] font-bold uppercase tracking-wider text-slate-400">Fuel Type</p>
                <p className="text-sm font-bold text-slate-800">{vehicle.fuelType}</p>
              </div>
            </div>

            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-blue-50 flex items-center justify-center text-blue-600 shrink-0">
                <Gauge className="w-5 h-5" />
              </div>
              <div>
                <p className="text-[10px] font-bold uppercase tracking-wider text-slate-400">Transmission</p>
                <p className="text-sm font-bold text-slate-800">{vehicle.transmission}</p>
              </div>
            </div>

            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-blue-50 flex items-center justify-center text-blue-600 shrink-0">
                <Users className="w-5 h-5" />
              </div>
              <div>
                <p className="text-[10px] font-bold uppercase tracking-wider text-slate-400">Capacity</p>
                <p className="text-sm font-bold text-slate-800">{vehicle.seatingCapacity} Passengers</p>
              </div>
            </div>

            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-blue-50 flex items-center justify-center text-blue-600 shrink-0">
                <Calendar className="w-5 h-5" />
              </div>
              <div>
                <p className="text-[10px] font-bold uppercase tracking-wider text-slate-400">Model Year</p>
                <p className="text-sm font-bold text-slate-800">{vehicle.modelYear}</p>
              </div>
            </div>
          </div>

          {/* Description */}
          <div className="bg-white rounded-2xl p-5 sm:p-6 border border-slate-200/80 shadow-sm space-y-3">
            <h3 className="text-base font-bold text-slate-900 flex items-center gap-2">
              <Crown className="w-4 h-4 text-amber-500" />
              <span>Wedding & Event Profile</span>
            </h3>
            <p className="text-sm text-slate-600 leading-relaxed">
              {vehicle.description || 'This celebration vehicle is kept in pristine condition with showroom finish polish, sanitized interiors, and full pre-event mechanical checks.'}
            </p>
          </div>

          {/* Included Features */}
          <div className="bg-white rounded-2xl p-5 sm:p-6 border border-slate-200/80 shadow-sm space-y-3">
            <h3 className="text-base font-bold text-slate-900">Included With Every Wedding Rental</h3>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 text-xs text-slate-600 pt-1">
              <div className="flex items-center gap-2">
                <CheckCircle className="w-4 h-4 text-emerald-500 shrink-0" />
                <span>Complimentary 24/7 Roadside & Chauffeur Coordination</span>
              </div>
              <div className="flex items-center gap-2">
                <CheckCircle className="w-4 h-4 text-emerald-500 shrink-0" />
                <span>Punctual Delivery to Wedding Venue or Resort</span>
              </div>
              <div className="flex items-center gap-2">
                <CheckCircle className="w-4 h-4 text-emerald-500 shrink-0" />
                <span>Florist-Friendly Decor & Ribbon Mounting Points</span>
              </div>
              <div className="flex items-center gap-2">
                <CheckCircle className="w-4 h-4 text-emerald-500 shrink-0" />
                <span>Spotless Pre-Event Detailing & Interior Sanitization</span>
              </div>
            </div>
          </div>

          {/* Customer Reviews & Ratings Section */}
          <div className="bg-white rounded-2xl p-5 sm:p-6 border border-slate-200/80 shadow-sm space-y-6">
            <div className="flex items-center justify-between pb-4 border-b border-slate-100">
              <div className="flex items-center gap-2">
                <MessageSquare className="w-5 h-5 text-blue-600" />
                <h3 className="text-base font-bold text-slate-900">
                  Customer Reviews & Wedding Experiences
                </h3>
              </div>
              <span className="text-xs font-semibold text-slate-500">
                {reviews.length} {reviews.length === 1 ? 'Review' : 'Reviews'}
              </span>
            </div>

            {/* Ratings Summary Card */}
            {ratingSummary && ratingSummary.totalReviews > 0 ? (
              <div className="grid grid-cols-1 sm:grid-cols-3 gap-6 p-5 rounded-2xl bg-slate-50 border border-slate-100 items-center">
                <div className="text-center sm:border-r sm:border-slate-200 sm:pr-4">
                  <div className="text-4xl font-black text-slate-900">
                    {ratingSummary.averageRating.toFixed(1)}
                  </div>
                  <div className="flex items-center justify-center gap-1 my-1.5">
                    {[1, 2, 3, 4, 5].map((s) => (
                      <Star
                        key={s}
                        className={`w-4 h-4 ${
                          s <= Math.round(ratingSummary.averageRating)
                            ? 'text-amber-500 fill-amber-500'
                            : 'text-slate-200'
                        }`}
                      />
                    ))}
                  </div>
                  <p className="text-[11px] text-slate-500 font-medium">
                    Based on {ratingSummary.totalReviews} verified rental{ratingSummary.totalReviews > 1 ? 's' : ''}
                  </p>
                </div>

                <div className="sm:col-span-2 space-y-1.5 text-xs text-slate-600">
                  {[5, 4, 3, 2, 1].map((stars) => {
                    const count = (ratingSummary.ratingCounts && ratingSummary.ratingCounts[stars]) || 0;
                    const percent = ratingSummary.totalReviews > 0 ? (count / ratingSummary.totalReviews) * 100 : 0;
                    return (
                      <div key={stars} className="flex items-center gap-2">
                        <span className="w-6 text-[11px] font-bold text-slate-600 flex items-center gap-0.5">
                          {stars}<Star className="w-2.5 h-2.5 fill-amber-500 text-amber-500" />
                        </span>
                        <div className="flex-1 h-2 bg-slate-200 rounded-full overflow-hidden">
                          <div
                            className="h-full bg-amber-500 rounded-full transition-all duration-500"
                            style={{ width: `${percent}%` }}
                          ></div>
                        </div>
                        <span className="w-8 text-right text-[10px] text-slate-400 font-mono">
                          {count}
                        </span>
                      </div>
                    );
                  })}
                </div>
              </div>
            ) : null}

            {/* Individual Reviews List */}
            {reviews.length === 0 ? (
              <div className="py-8 text-center text-slate-400 text-xs space-y-1">
                <Star className="w-8 h-8 mx-auto text-slate-300 stroke-1" />
                <p className="font-semibold text-slate-700">No reviews yet for this vehicle</p>
                <p className="text-[11px] text-slate-400">
                  Customers who complete their wedding reservations can submit verified ratings and photos.
                </p>
              </div>
            ) : (
              <div className="space-y-4 divide-y divide-slate-100">
                {reviews.map((rev) => (
                  <div key={rev.id} className="pt-4 first:pt-0 space-y-2">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2.5">
                        <div className="w-8 h-8 rounded-full bg-blue-100 text-blue-700 font-bold text-xs flex items-center justify-center">
                          {rev.customerName ? rev.customerName.charAt(0).toUpperCase() : 'G'}
                        </div>
                        <div>
                          <div className="flex items-center gap-2">
                            <h4 className="text-xs font-bold text-slate-900">{rev.customerName}</h4>
                            <span className="px-1.5 py-0.5 rounded text-[10px] bg-emerald-50 text-emerald-700 font-semibold border border-emerald-200">
                              Verified Wedding Guest
                            </span>
                          </div>
                          <p className="text-[10px] text-slate-400">
                            {rev.createdAt ? new Date(rev.createdAt).toLocaleDateString('en-IN', { month: 'short', day: 'numeric', year: 'numeric' }) : 'Recent'}
                          </p>
                        </div>
                      </div>

                      {/* Stars */}
                      <div className="flex items-center gap-0.5">
                        {[1, 2, 3, 4, 5].map((s) => (
                          <Star
                            key={s}
                            className={`w-3.5 h-3.5 ${
                              s <= rev.rating
                                ? 'text-amber-500 fill-amber-500'
                                : 'text-slate-200'
                            }`}
                          />
                        ))}
                      </div>
                    </div>

                    <p className="text-xs text-slate-600 leading-relaxed pl-10">
                      "{rev.comment}"
                    </p>
                  </div>
                ))}
              </div>
            )}
          </div>

        </div>

        {/* Right Column: Booking Card */}
        <div className="space-y-6">
          <div className="bg-white rounded-3xl p-6 sm:p-8 border border-slate-200 shadow-xl sticky top-24">
            
            {/* Price Header */}
            <div className="pb-6 border-b border-slate-100 flex items-baseline justify-between">
              <div>
                <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">Rental Price</p>
                <div className="flex items-baseline gap-1.5 mt-1">
                  <span className="text-2xl sm:text-3xl font-black text-slate-900">₹ {formattedPrice}</span>
                  <span className="text-xs text-slate-500 font-medium">/ day</span>
                </div>
              </div>
              <div className="text-right">
                <span className="px-2.5 py-1 text-[11px] font-semibold bg-blue-50 text-blue-700 rounded-lg">
                  Instant Request
                </span>
              </div>
            </div>

            {/* Booking Success View */}
            {bookingSuccess ? (
              <div className="py-6 space-y-4 text-center">
                <div className="w-12 h-12 bg-emerald-50 text-emerald-600 rounded-full flex items-center justify-center mx-auto">
                  <CheckCircle className="w-6 h-6" />
                </div>
                <h4 className="text-lg font-bold text-slate-900">Celebration Request Submitted!</h4>
                <p className="text-xs text-slate-500 leading-relaxed">
                  Your reservation reference is <strong className="text-slate-800 font-mono">{bookingSuccess.bookingReference}</strong>. Our fleet administrator has received your wedding vehicle request.
                </p>
                <div className="pt-2 flex flex-col gap-2">
                  <Link
                    to="/my-bookings"
                    className="w-full py-2.5 bg-blue-600 hover:bg-blue-700 text-white text-xs font-bold rounded-xl transition shadow"
                  >
                    View In My Bookings
                  </Link>
                  <button
                    onClick={() => setBookingSuccess(null)}
                    className="w-full py-2.5 bg-slate-100 text-slate-700 text-xs font-bold rounded-xl"
                  >
                    Book Another Rental
                  </button>
                </div>
              </div>
            ) : (
              /* Booking Form */
              <form onSubmit={handleBookingSubmit} className="pt-6 space-y-4">
                
                {bookingError && (
                  <div className="p-3 rounded-xl bg-rose-50 border border-rose-200 text-rose-700 text-xs flex items-start gap-2">
                    <AlertCircle className="w-4 h-4 shrink-0 mt-0.5" />
                    <span>{bookingError}</span>
                  </div>
                )}

                <div>
                  <label className="block text-xs font-bold text-slate-600 uppercase tracking-wider mb-1.5">
                    Pickup Date
                  </label>
                  <input
                    type="date"
                    min={today}
                    value={pickupDate}
                    onChange={(e) => setPickupDate(e.target.value)}
                    required
                    className="w-full px-3 py-2.5 text-xs font-medium rounded-xl border border-slate-200 focus:ring-2 focus:ring-blue-500 bg-slate-50"
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-600 uppercase tracking-wider mb-1.5">
                    Return Date
                  </label>
                  <input
                    type="date"
                    min={pickupDate || today}
                    value={returnDate}
                    onChange={(e) => setReturnDate(e.target.value)}
                    required
                    className="w-full px-3 py-2.5 text-xs font-medium rounded-xl border border-slate-200 focus:ring-2 focus:ring-blue-500 bg-slate-50"
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-600 uppercase tracking-wider mb-1.5">
                    Venue or City Address
                  </label>
                  <input
                    type="text"
                    value={pickupLocation}
                    onChange={(e) => setPickupLocation(e.target.value)}
                    required
                    placeholder="e.g. Grand Resort, Chandigarh or Aerocity, Delhi"
                    className="w-full px-3 py-2.5 text-xs font-medium rounded-xl border border-slate-200 focus:ring-2 focus:ring-blue-500 bg-slate-50"
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-600 uppercase tracking-wider mb-1.5">
                    Celebration Notes (Optional)
                  </label>
                  <textarea
                    rows={2}
                    value={notes}
                    onChange={(e) => setNotes(e.target.value)}
                    placeholder="e.g. Groom entry ribbons required, morning muhurat pickup"
                    className="w-full px-3 py-2 text-xs font-medium rounded-xl border border-slate-200 focus:ring-2 focus:ring-blue-500 bg-slate-50"
                  />
                </div>

                {/* Live Calculation Display */}
                {calcLoading ? (
                  <div className="p-3 bg-slate-50 rounded-xl text-center text-xs text-slate-500">
                    Calculating rental charge...
                  </div>
                ) : calculation ? (
                  <div className="p-4 bg-blue-50/60 rounded-2xl border border-blue-100 space-y-2 text-xs">
                    <div className="flex justify-between text-slate-600">
                      <span>Duration:</span>
                      <span className="font-bold text-slate-900">{calculation.numberOfDays} days</span>
                    </div>
                    <div className="flex justify-between text-slate-600">
                      <span>Rate per day:</span>
                      <span className="font-semibold text-slate-900">₹ {Number(calculation.pricePerDay).toLocaleString('en-IN')}</span>
                    </div>
                    <div className="pt-2 border-t border-blue-200 flex justify-between text-sm font-bold text-slate-900">
                      <span>Total Rental:</span>
                      <span className="text-blue-700 font-extrabold text-base">₹ {Number(calculation.totalAmount).toLocaleString('en-IN')}</span>
                    </div>
                    {!calculation.isAvailable && (
                      <p className="text-rose-600 font-bold text-[11px] pt-1">
                        ⚠️ Vehicle is already booked for these celebration dates.
                      </p>
                    )}
                  </div>
                ) : null}

                <button
                  type="submit"
                  disabled={bookingLoading || (calculation && !calculation.isAvailable) || !vehicle.available}
                  className="w-full py-3 px-4 bg-blue-600 hover:bg-blue-700 disabled:bg-slate-300 text-white font-bold rounded-xl text-xs sm:text-sm transition shadow-lg shadow-blue-500/20 flex items-center justify-center gap-2 mt-2"
                >
                  {bookingLoading ? (
                    <span className="animate-spin w-4 h-4 border-2 border-white border-t-transparent rounded-full"></span>
                  ) : user ? (
                    <span>Confirm & Reserve Wedding Vehicle</span>
                  ) : (
                    <span>Sign In to Request Booking</span>
                  )}
                </button>

                <p className="text-[11px] text-center text-slate-400 mt-2">
                  No advance payment required. Backend calculates all final charges.
                </p>

              </form>
            )}

          </div>
        </div>

      </div>

    </div>
  );
}
