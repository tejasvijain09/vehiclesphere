import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../services/api';
import VehicleCard from '../components/VehicleCard';
import { 
  Search, 
  Sparkles, 
  Car, 
  ChevronRight,
  Heart,
  CalendarCheck,
  ShieldCheck,
  Award,
  Crown
} from 'lucide-react';

export default function HomePage() {
  const navigate = useNavigate();
  const [featuredVehicles, setFeaturedVehicles] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  // Search form states
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [selectedFuel, setSelectedFuel] = useState('');

  useEffect(() => {
    const fetchHomeData = async () => {
      try {
        const [vehiclesRes, catsRes] = await Promise.all([
          api.get('/vehicles?availableOnly=true'),
          api.get('/categories')
        ]);
        setFeaturedVehicles(vehiclesRes.data.slice(0, 6));
        setCategories(catsRes.data);
      } catch (err) {
        console.error('Failed to load homepage data', err);
      } finally {
        setLoading(false);
      }
    };
    fetchHomeData();
  }, []);

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    const params = new URLSearchParams();
    if (searchQuery.trim()) params.append('search', searchQuery.trim());
    if (selectedCategory) params.append('categoryId', selectedCategory);
    if (selectedFuel) params.append('fuelType', selectedFuel);
    navigate(`/vehicles?${params.toString()}`);
  };

  return (
    <div className="w-full space-y-16 sm:space-y-20 pb-20 overflow-x-hidden">
      
      {/* Hero Section */}
      <section className="relative overflow-hidden bg-gradient-to-b from-slate-900 via-slate-900 to-slate-800 text-white pt-16 sm:pt-20 pb-28 sm:pb-32">
        <div className="absolute inset-0 opacity-15 bg-[radial-gradient(#3b82f6_1px,transparent_1px)] [background-size:24px_24px] pointer-events-none"></div>
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
          <div className="max-w-3xl mx-auto text-center space-y-5 sm:space-y-6">
            
            <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full bg-blue-500/15 border border-blue-400/30 text-blue-300 text-xs font-bold uppercase tracking-wider shadow-sm">
              <Crown className="w-3.5 h-3.5 text-amber-400" />
              <span>PREMIUM WEDDING & EVENT VEHICLE RENTALS</span>
            </div>

            <h1 className="text-3xl sm:text-5xl md:text-6xl font-extrabold tracking-tight text-white leading-tight sm:leading-tight">
              Make Every Wedding Arrival <br className="hidden sm:inline" />
              <span className="text-transparent bg-clip-text bg-gradient-to-r from-blue-400 via-indigo-300 to-amber-200">
                Unforgettable.
              </span>
            </h1>

            <p className="text-sm sm:text-lg text-slate-300 max-w-2xl mx-auto font-normal leading-relaxed">
              Book elegant bridal cars, luxury SUVs, family vehicles, and premium electric cars for weddings, receptions, airport pickups, and special celebrations across India.
            </p>

          </div>

          {/* Quick Search Card */}
          <div className="mt-10 sm:mt-12 max-w-4xl mx-auto bg-white rounded-2xl shadow-2xl p-4 sm:p-6 text-slate-800 border border-slate-100">
            <form onSubmit={handleSearchSubmit} className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 items-end">
              
              <div className="w-full">
                <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1.5">
                  Search Wedding Vehicle
                </label>
                <div className="relative">
                  <Search className="w-4 h-4 text-slate-400 absolute left-3 top-3.5" />
                  <input
                    type="text"
                    placeholder="e.g. Fortuner, BMW, Thar..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="w-full pl-9 pr-3 py-2.5 text-xs sm:text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 bg-slate-50"
                  />
                </div>
              </div>

              <div className="w-full">
                <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1.5">
                  Category
                </label>
                <select
                  value={selectedCategory}
                  onChange={(e) => setSelectedCategory(e.target.value)}
                  className="w-full px-3 py-2.5 text-xs sm:text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 bg-slate-50"
                >
                  <option value="">All Categories</option>
                  {categories.map((c) => (
                    <option key={c.id} value={c.id}>{c.name}</option>
                  ))}
                </select>
              </div>

              <div className="w-full">
                <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1.5">
                  Fuel Type
                </label>
                <select
                  value={selectedFuel}
                  onChange={(e) => setSelectedFuel(e.target.value)}
                  className="w-full px-3 py-2.5 text-xs sm:text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 bg-slate-50"
                >
                  <option value="">Any Fuel Type</option>
                  <option value="Electric">Electric</option>
                  <option value="Petrol">Petrol</option>
                  <option value="Diesel">Diesel</option>
                  <option value="Hybrid">Hybrid</option>
                </select>
              </div>

              <div className="w-full">
                <button
                  type="submit"
                  className="w-full py-2.5 px-4 bg-blue-600 hover:bg-blue-700 text-white text-xs sm:text-sm font-bold rounded-xl transition shadow-md shadow-blue-500/20 flex items-center justify-center gap-2"
                >
                  <Search className="w-4 h-4" />
                  <span>Find Vehicles</span>
                </button>
              </div>

            </form>
          </div>

        </div>
      </section>

      {/* Categories Showcase */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full">
        <div className="flex flex-col sm:flex-row sm:items-end justify-between mb-8 gap-3">
          <div>
            <h2 className="text-xs font-bold uppercase tracking-wider text-blue-600 mb-1">Celebration Fleet</h2>
            <h3 className="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">
              Curated For Every Wedding Function
            </h3>
          </div>
          <Link
            to="/vehicles"
            className="text-xs sm:text-sm font-bold text-blue-600 hover:text-blue-700 inline-flex items-center gap-1 group self-start sm:self-auto"
          >
            <span>View all fleet</span>
            <ChevronRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
          </Link>
        </div>

        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-3 sm:gap-4">
          {categories.map((cat) => (
            <Link
              key={cat.id}
              to={`/vehicles?categoryId=${cat.id}`}
              className="p-4 sm:p-5 bg-white rounded-2xl border border-slate-200/80 hover:border-blue-400 hover:shadow-md transition text-center group flex flex-col items-center justify-between"
            >
              <div className="w-12 h-12 rounded-xl bg-blue-50 group-hover:bg-blue-600 text-blue-600 group-hover:text-white flex items-center justify-center transition mb-3">
                <Car className="w-6 h-6" />
              </div>
              <div>
                <h4 className="text-sm font-bold text-slate-900 group-hover:text-blue-600 transition">
                  {cat.name}
                </h4>
                <p className="text-[11px] text-slate-500 mt-1 line-clamp-2">
                  {cat.description}
                </p>
              </div>
            </Link>
          ))}
        </div>
      </section>

      {/* Featured Vehicles Grid */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full">
        <div className="flex flex-col sm:flex-row sm:items-end justify-between mb-8 gap-3">
          <div>
            <h2 className="text-xs font-bold uppercase tracking-wider text-blue-600 mb-1">Ready For Reservation</h2>
            <h3 className="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">
              Popular Wedding & Event Rentals
            </h3>
          </div>
          <Link
            to="/vehicles"
            className="text-xs sm:text-sm font-bold text-blue-600 hover:text-blue-700 inline-flex items-center gap-1 group self-start sm:self-auto"
          >
            <span>Browse complete catalog</span>
            <ChevronRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
          </Link>
        </div>

        {loading ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {[1, 2, 3].map((i) => (
              <div key={i} className="h-80 bg-slate-200 animate-pulse rounded-2xl"></div>
            ))}
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {featuredVehicles.map((vehicle) => (
              <VehicleCard key={vehicle.id} vehicle={vehicle} />
            ))}
          </div>
        )}
      </section>

      {/* How It Works - Wedding Centric */}
      <section className="bg-slate-900 text-white py-16 sm:py-20 w-full">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-2xl mx-auto mb-12 sm:mb-16">
            <h2 className="text-xs font-bold uppercase tracking-wider text-blue-400 mb-2">Hassle-Free Reservations</h2>
            <h3 className="text-2xl sm:text-3xl font-extrabold tracking-tight">How VehicleSphere Works</h3>
            <p className="text-slate-400 text-xs sm:text-sm mt-3">
              Streamlined three-step vehicle booking engineered for punctuality and elegance on your special day.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 sm:gap-8">
            <div className="bg-slate-800/60 border border-slate-700/60 p-6 sm:p-8 rounded-2xl relative">
              <div className="w-12 h-12 rounded-xl bg-blue-600 flex items-center justify-center font-extrabold text-lg mb-6 shadow-md shadow-blue-500/20">
                1
              </div>
              <h4 className="text-base sm:text-lg font-bold mb-2">Choose Your Wedding Vehicle</h4>
              <p className="text-xs sm:text-sm text-slate-400 leading-relaxed">
                Browse luxury bridal cars, baraat SUVs, and executive sedans with verified availability, Indian location stations, and transparent daily Rupee rates.
              </p>
            </div>

            <div className="bg-slate-800/60 border border-slate-700/60 p-6 sm:p-8 rounded-2xl relative">
              <div className="w-12 h-12 rounded-xl bg-blue-600 flex items-center justify-center font-extrabold text-lg mb-6 shadow-md shadow-blue-500/20">
                2
              </div>
              <h4 className="text-base sm:text-lg font-bold mb-2">Select Event Dates & Location</h4>
              <p className="text-xs sm:text-sm text-slate-400 leading-relaxed">
                Pick your ceremony dates with automated backend rate calculation and guaranteed conflict prevention so your ride is exclusively yours.
              </p>
            </div>

            <div className="bg-slate-800/60 border border-slate-700/60 p-6 sm:p-8 rounded-2xl relative">
              <div className="w-12 h-12 rounded-xl bg-blue-600 flex items-center justify-center font-extrabold text-lg mb-6 shadow-md shadow-blue-500/20">
                3
              </div>
              <h4 className="text-base sm:text-lg font-bold mb-2">Reserve for Your Celebration</h4>
              <p className="text-xs sm:text-sm text-slate-400 leading-relaxed">
                Submit your booking with zero advance payment required. Receive prompt admin approval and delivery to your wedding resort or home venue.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Academic / DevOps Project Highlight Box */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full">
        <div className="bg-gradient-to-r from-blue-700 via-blue-600 to-indigo-700 rounded-3xl p-6 sm:p-12 text-white shadow-xl relative overflow-hidden">
          <div className="relative z-10 max-w-2xl space-y-4">
            <span className="px-3 py-1 bg-white/20 backdrop-blur rounded-full text-xs font-semibold uppercase tracking-wider inline-block">
              Academic Demonstration Architecture
            </span>
            <h3 className="text-2xl sm:text-3xl font-extrabold tracking-tight">
              Cloud-Native Stack with Automated CI/CD & Observability
            </h3>
            <p className="text-blue-100 text-xs sm:text-sm leading-relaxed">
              VehicleSphere demonstrates modern enterprise software engineering: Spring Boot 3 modular architecture, PostgreSQL relational schema with Flyway migrations, stateless JWT security, Criteria Specifications, and responsive React 18 UI.
            </p>
            <div className="flex flex-wrap gap-3 pt-2">
              <Link
                to="/vehicles"
                className="px-5 py-2.5 bg-white text-blue-700 font-bold rounded-xl text-xs shadow hover:bg-blue-50 transition"
              >
                Browse Fleet Catalog
              </Link>
              <Link
                to="/login"
                className="px-5 py-2.5 bg-blue-800 hover:bg-blue-900 text-white font-bold rounded-xl text-xs transition border border-blue-500"
              >
                Demo Sign In
              </Link>
            </div>
          </div>
        </div>
      </section>

    </div>
  );
}
