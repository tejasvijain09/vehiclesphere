import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import StatusBadge from '../../components/StatusBadge';
import { 
  Search, 
  Check, 
  X, 
  Play, 
  CheckCheck, 
  XCircle, 
  AlertCircle, 
  CheckCircle,
  Calendar,
  MapPin,
  Car
} from 'lucide-react';

export default function AdminBookingsPage() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('ALL');
  const [search, setSearch] = useState('');
  const [actionLoading, setActionLoading] = useState(false);
  const [toast, setToast] = useState(null);

  const fetchBookings = async () => {
    try {
      const res = await api.get('/admin/bookings');
      setBookings(res.data);
    } catch (err) {
      console.error('Failed to load bookings', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBookings();
  }, []);

  const handleStatusUpdate = async (bookingId, newStatus, reason = '') => {
    setActionLoading(true);
    try {
      await api.patch(`/admin/bookings/${bookingId}/status`, {
        status: newStatus,
        notes: reason || `Status updated to ${newStatus} by admin`,
      });
      setToast({
        type: 'success',
        message: `Booking status updated to ${newStatus}.`,
      });
      fetchBookings();
    } catch (err) {
      setToast({
        type: 'error',
        message: err.response?.data?.message || 'Failed to update booking status.',
      });
    } finally {
      setActionLoading(false);
    }
  };

  const filteredBookings = bookings.filter((b) => {
    const matchesTab = activeTab === 'ALL' || b.status === activeTab;
    const matchesSearch =
      b.bookingReference.toLowerCase().includes(search.toLowerCase()) ||
      b.customerName.toLowerCase().includes(search.toLowerCase()) ||
      b.vehicleName.toLowerCase().includes(search.toLowerCase()) ||
      b.customerEmail.toLowerCase().includes(search.toLowerCase());
    return matchesTab && matchesSearch;
  });

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      
      {/* Header */}
      <div>
        <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">
          Customer Booking Management
        </h1>
        <p className="text-sm text-slate-500 mt-1">
          Review reservation requests, approve or reject rentals, and update live ride lifecycles.
        </p>
      </div>

      {/* Toast Alert */}
      {toast && (
        <div
          className={`p-4 rounded-2xl text-xs flex items-center justify-between border ${
            toast.type === 'success'
              ? 'bg-emerald-50 text-emerald-800 border-emerald-200'
              : 'bg-rose-50 text-rose-800 border-rose-200'
          }`}
        >
          <div className="flex items-center gap-2">
            {toast.type === 'success' ? (
              <CheckCircle className="w-4 h-4 text-emerald-600" />
            ) : (
              <AlertCircle className="w-4 h-4 text-rose-600" />
            )}
            <span>{toast.message}</span>
          </div>
          <button onClick={() => setToast(null)} className="p-1 text-slate-400 hover:text-slate-600">
            <X className="w-4 h-4" />
          </button>
        </div>
      )}

      {/* Filters & Tabs */}
      <div className="bg-white rounded-2xl p-4 border border-slate-200/80 shadow-sm flex flex-col sm:flex-row items-center gap-4 justify-between">
        <div className="relative w-full sm:w-80">
          <Search className="w-4 h-4 text-slate-400 absolute left-3 top-3" />
          <input
            type="text"
            placeholder="Search ref #, customer, car..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full pl-9 pr-3 py-2 text-xs rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 bg-slate-50"
          />
        </div>

        <div className="flex items-center gap-1.5 overflow-x-auto w-full sm:w-auto pb-1 sm:pb-0 text-xs font-semibold">
          {['ALL', 'PENDING', 'CONFIRMED', 'ACTIVE', 'COMPLETED', 'CANCELLED', 'REJECTED'].map((tab) => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`px-3 py-1.5 rounded-lg transition shrink-0 ${
                activeTab === tab
                  ? 'bg-slate-900 text-white shadow-sm'
                  : 'text-slate-600 hover:bg-slate-100'
              }`}
            >
              {tab}
            </button>
          ))}
        </div>
      </div>

      {/* Bookings Table */}
      <div className="bg-white rounded-3xl border border-slate-200/80 shadow-sm overflow-hidden">
        {loading ? (
          <div className="py-20 text-center">
            <div className="w-10 h-10 border-4 border-blue-600 border-t-transparent rounded-full animate-spin mx-auto"></div>
            <p className="text-sm text-slate-500 mt-3">Loading booking records...</p>
          </div>
        ) : filteredBookings.length === 0 ? (
          <div className="p-12 text-center text-slate-400 text-xs">
            No booking records match this query.
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead className="bg-slate-50 text-slate-500 font-bold uppercase tracking-wider border-b border-slate-100">
                <tr>
                  <th className="px-6 py-4">Reference</th>
                  <th className="px-6 py-4">Customer</th>
                  <th className="px-6 py-4">Vehicle</th>
                  <th className="px-6 py-4">Dates & Location</th>
                  <th className="px-6 py-4">Amount</th>
                  <th className="px-6 py-4">Status</th>
                  <th className="px-6 py-4 text-right">Workflow Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filteredBookings.map((b) => (
                  <tr key={b.id} className="hover:bg-slate-50/80 transition">
                    <td className="px-6 py-4 font-mono font-bold text-slate-900">
                      {b.bookingReference}
                    </td>
                    <td className="px-6 py-4">
                      <p className="font-bold text-slate-900">{b.customerName}</p>
                      <p className="text-[11px] text-slate-400">{b.customerEmail}</p>
                      <p className="text-[10px] text-slate-400">{b.customerPhone}</p>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-3">
                        <img
                          src={b.vehicleImageUrl}
                          alt={b.vehicleName}
                          className="w-10 h-10 rounded-lg object-cover bg-slate-100 shrink-0"
                          onError={(e) => {
                            e.target.src = 'https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=200&q=80';
                          }}
                        />
                        <div>
                          <p className="font-bold text-slate-900">{b.vehicleName}</p>
                          <p className="text-[11px] font-mono text-slate-400">{b.vehicleRegistrationNumber}</p>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-slate-600">
                      <p className="font-medium">{b.pickupDate} → {b.returnDate}</p>
                      <p className="text-[11px] text-slate-400">{b.numberOfDays} days</p>
                      <div className="flex items-center gap-1 text-[11px] text-slate-500 mt-0.5 truncate max-w-[150px]">
                        <MapPin className="w-3 h-3 text-slate-400 shrink-0" />
                        <span className="truncate">{b.pickupLocation}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <p className="font-black text-slate-900 text-sm">₹ {Number(b.totalAmount).toLocaleString('en-IN')}</p>
                      <p className="text-[10px] text-slate-400">₹ {Number(b.pricePerDay).toLocaleString('en-IN')} / day</p>
                    </td>
                    <td className="px-6 py-4">
                      <StatusBadge status={b.status} />
                    </td>
                    <td className="px-6 py-4 text-right">
                      <div className="inline-flex items-center gap-1.5">
                        
                        {/* PENDING -> Approve or Reject */}
                        {b.status === 'PENDING' && (
                          <>
                            <button
                              disabled={actionLoading}
                              onClick={() => handleStatusUpdate(b.id, 'CONFIRMED')}
                              className="px-2.5 py-1 bg-emerald-600 hover:bg-emerald-700 text-white rounded-lg font-bold text-[11px] shadow-sm flex items-center gap-1"
                              title="Approve Booking"
                            >
                              <Check className="w-3 h-3" />
                              <span>Approve</span>
                            </button>
                            <button
                              disabled={actionLoading}
                              onClick={() => handleStatusUpdate(b.id, 'REJECTED')}
                              className="px-2.5 py-1 bg-rose-600 hover:bg-rose-700 text-white rounded-lg font-bold text-[11px] shadow-sm flex items-center gap-1"
                              title="Reject Booking"
                            >
                              <X className="w-3 h-3" />
                              <span>Reject</span>
                            </button>
                          </>
                        )}

                        {/* CONFIRMED -> Mark ACTIVE */}
                        {b.status === 'CONFIRMED' && (
                          <button
                            disabled={actionLoading}
                            onClick={() => handleStatusUpdate(b.id, 'ACTIVE')}
                            className="px-2.5 py-1 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-bold text-[11px] shadow-sm flex items-center gap-1"
                            title="Vehicle Handed Over (Active)"
                          >
                            <Play className="w-3 h-3" />
                            <span>Mark Active</span>
                          </button>
                        )}

                        {/* ACTIVE -> Mark COMPLETED */}
                        {b.status === 'ACTIVE' && (
                          <button
                            disabled={actionLoading}
                            onClick={() => handleStatusUpdate(b.id, 'COMPLETED')}
                            className="px-2.5 py-1 bg-purple-600 hover:bg-purple-700 text-white rounded-lg font-bold text-[11px] shadow-sm flex items-center gap-1"
                            title="Vehicle Returned (Completed)"
                          >
                            <CheckCheck className="w-3 h-3" />
                            <span>Mark Completed</span>
                          </button>
                        )}

                        {/* If CONFIRMED, admin can also cancel */}
                        {b.status === 'CONFIRMED' && (
                          <button
                            disabled={actionLoading}
                            onClick={() => handleStatusUpdate(b.id, 'CANCELLED')}
                            className="p-1 text-slate-400 hover:text-rose-600 rounded"
                            title="Cancel Booking"
                          >
                            <XCircle className="w-4 h-4" />
                          </button>
                        )}

                        {/* Completed, Cancelled, Rejected states have no further action */}
                        {['COMPLETED', 'CANCELLED', 'REJECTED'].includes(b.status) && (
                          <span className="text-[11px] text-slate-400 italic">No action</span>
                        )}

                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

    </div>
  );
}
