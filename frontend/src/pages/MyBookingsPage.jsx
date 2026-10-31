import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api';
import StatusBadge from '../components/StatusBadge';
import { 
  CalendarCheck, 
  MapPin, 
  Calendar, 
  Car, 
  X, 
  AlertCircle, 
  CheckCircle, 
  Clock 
} from 'lucide-react';

export default function MyBookingsPage() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('ALL');
  const [cancelModalBooking, setCancelModalBooking] = useState(null);
  const [cancelLoading, setCancelLoading] = useState(false);
  const [notification, setNotification] = useState(null);

  const fetchBookings = async () => {
    try {
      const res = await api.get('/bookings/my');
      setBookings(res.data);
    } catch (err) {
      console.error('Failed to load my bookings', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBookings();
  }, []);

  const handleCancel = async () => {
    if (!cancelModalBooking) return;
    setCancelLoading(true);
    try {
      await api.patch(`/bookings/${cancelModalBooking.id}/cancel`);
      setNotification({
        type: 'success',
        message: `Booking #${cancelModalBooking.bookingReference} has been cancelled successfully.`,
      });
      setCancelModalBooking(null);
      fetchBookings();
    } catch (err) {
      setNotification({
        type: 'error',
        message: err.response?.data?.message || 'Failed to cancel booking.',
      });
    } finally {
      setCancelLoading(false);
    }
  };

  const filteredBookings = bookings.filter((b) => {
    if (activeTab === 'ALL') return true;
    return b.status === activeTab;
  });

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">
            My Wedding & Event Reservations
          </h1>
          <p className="text-sm text-slate-500 mt-1">
            Review and manage all your celebration vehicle bookings and rental requests.
          </p>
        </div>
        <Link
          to="/vehicles"
          className="px-5 py-2.5 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-xl text-xs shadow-sm self-start sm:self-auto"
        >
          Book Another Vehicle
        </Link>
      </div>

      {/* Notifications */}
      {notification && (
        <div
          className={`p-4 rounded-2xl text-xs flex items-center justify-between border ${
            notification.type === 'success'
              ? 'bg-emerald-50 text-emerald-800 border-emerald-200'
              : 'bg-rose-50 text-rose-800 border-rose-200'
          }`}
        >
          <div className="flex items-center gap-2">
            {notification.type === 'success' ? (
              <CheckCircle className="w-4 h-4 text-emerald-600" />
            ) : (
              <AlertCircle className="w-4 h-4 text-rose-600" />
            )}
            <span>{notification.message}</span>
          </div>
          <button
            onClick={() => setNotification(null)}
            className="p-1 text-slate-400 hover:text-slate-600"
          >
            <X className="w-4 h-4" />
          </button>
        </div>
      )}

      {/* Status Filter Tabs */}
      <div className="flex items-center gap-2 overflow-x-auto pb-2 border-b border-slate-200 text-xs font-semibold">
        {['ALL', 'PENDING', 'CONFIRMED', 'ACTIVE', 'COMPLETED', 'CANCELLED'].map((tab) => (
          <button
            key={tab}
            onClick={() => setActiveTab(tab)}
            className={`px-4 py-2 rounded-xl transition shrink-0 ${
              activeTab === tab
                ? 'bg-slate-900 text-white shadow-sm'
                : 'text-slate-600 hover:bg-slate-100'
            }`}
          >
            {tab}
            <span className="ml-1.5 opacity-70 text-[10px]">
              ({tab === 'ALL' ? bookings.length : bookings.filter((b) => b.status === tab).length})
            </span>
          </button>
        ))}
      </div>

      {/* Bookings List */}
      {loading ? (
        <div className="py-20 text-center">
          <div className="w-10 h-10 border-4 border-blue-600 border-t-transparent rounded-full animate-spin mx-auto"></div>
          <p className="text-sm text-slate-500 mt-3">Loading your bookings...</p>
        </div>
      ) : filteredBookings.length === 0 ? (
        <div className="bg-white rounded-3xl p-16 text-center border border-slate-200/80 shadow-sm">
          <Car className="w-12 h-12 text-slate-300 mx-auto mb-3" />
          <h3 className="text-base font-bold text-slate-800">No {activeTab !== 'ALL' ? activeTab.toLowerCase() : ''} bookings found</h3>
          <p className="text-xs text-slate-500 mt-1">Ready to hit the road? Discover available vehicles right now.</p>
          <Link
            to="/vehicles"
            className="inline-block mt-4 px-4 py-2 bg-blue-600 text-white rounded-xl text-xs font-bold shadow"
          >
            Browse Available Vehicles
          </Link>
        </div>
      ) : (
        <div className="space-y-4">
          {filteredBookings.map((b) => (
            <div
              key={b.id}
              className="bg-white rounded-2xl border border-slate-200/80 p-5 sm:p-6 shadow-sm hover:shadow transition flex flex-col md:flex-row md:items-center justify-between gap-6"
            >
              {/* Left: Vehicle Image & Info */}
              <div className="flex items-start sm:items-center gap-4">
                <img
                  src={b.vehicleImageUrl}
                  alt={b.vehicleName}
                  className="w-20 h-20 sm:w-24 sm:h-24 rounded-2xl object-cover bg-slate-100 shrink-0 border border-slate-100"
                  onError={(e) => {
                    e.target.src = 'https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=300&q=80';
                  }}
                />
                <div className="space-y-1">
                  <div className="flex items-center gap-2">
                    <span className="font-mono text-xs font-bold text-blue-600">{b.bookingReference}</span>
                    <StatusBadge status={b.status} />
                  </div>
                  <h3 className="text-base font-bold text-slate-900">{b.vehicleName}</h3>
                  <div className="flex flex-wrap items-center gap-y-1 gap-x-3 text-xs text-slate-500">
                    <span className="flex items-center gap-1">
                      <Calendar className="w-3.5 h-3.5 text-slate-400" />
                      {b.pickupDate} to {b.returnDate} ({b.numberOfDays} days)
                    </span>
                    <span>•</span>
                    <span className="flex items-center gap-1">
                      <MapPin className="w-3.5 h-3.5 text-slate-400" />
                      {b.pickupLocation}
                    </span>
                  </div>
                  {b.notes && (
                    <p className="text-[11px] text-slate-400 italic pt-1">
                      Note: {b.notes}
                    </p>
                  )}
                </div>
              </div>

              {/* Right: Pricing & Actions */}
              <div className="flex sm:flex-col items-center sm:items-end justify-between border-t sm:border-t-0 pt-4 sm:pt-0 border-slate-100 gap-3">
                <div className="text-left sm:text-right">
                  <p className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Total Charge</p>
                  <p className="text-xl sm:text-2xl font-black text-slate-900">₹ {Number(b.totalAmount).toLocaleString('en-IN')}</p>
                  <p className="text-[11px] text-slate-400">₹ {Number(b.pricePerDay).toLocaleString('en-IN')} × {b.numberOfDays} days</p>
                </div>

                <div>
                  {(b.status === 'PENDING' || b.status === 'CONFIRMED') && (
                    <button
                      onClick={() => setCancelModalBooking(b)}
                      className="px-3.5 py-1.5 rounded-xl border border-rose-200 text-rose-600 hover:bg-rose-50 text-xs font-bold transition"
                    >
                      Cancel Booking
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Cancellation Confirmation Modal */}
      {cancelModalBooking && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-sm">
          <div className="bg-white rounded-3xl max-w-md w-full p-6 shadow-2xl border border-slate-100 space-y-4">
            <div className="w-12 h-12 rounded-full bg-rose-50 text-rose-600 flex items-center justify-center mx-auto">
              <AlertCircle className="w-6 h-6" />
            </div>
            <div className="text-center">
              <h3 className="text-lg font-bold text-slate-900">Cancel Reservation?</h3>
              <p className="text-xs text-slate-500 mt-1">
                Are you sure you want to cancel booking <strong className="font-mono text-slate-800">{cancelModalBooking.bookingReference}</strong> for {cancelModalBooking.vehicleName}?
              </p>
            </div>
            <div className="grid grid-cols-2 gap-3 pt-2">
              <button
                type="button"
                onClick={() => setCancelModalBooking(null)}
                className="py-2.5 px-4 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold rounded-xl text-xs transition"
              >
                Keep Booking
              </button>
              <button
                type="button"
                disabled={cancelLoading}
                onClick={handleCancel}
                className="py-2.5 px-4 bg-rose-600 hover:bg-rose-700 text-white font-bold rounded-xl text-xs transition shadow-md shadow-rose-500/20"
              >
                {cancelLoading ? 'Cancelling...' : 'Confirm Cancel'}
              </button>
            </div>
          </div>
        </div>
      )}

    </div>
  );
}
