import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import StatusBadge from '../components/StatusBadge';
import { 
  CalendarCheck, 
  Clock, 
  CheckCircle2, 
  XCircle, 
  Car, 
  ArrowRight,
  TrendingUp,
  MapPin
} from 'lucide-react';

export default function CustomerDashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        const res = await api.get('/customer/dashboard');
        setStats(res.data);
      } catch (err) {
        console.error('Failed to load dashboard', err);
      } finally {
        setLoading(false);
      }
    };
    fetchDashboard();
  }, []);

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-16 text-center">
        <div className="w-10 h-10 border-4 border-blue-600 border-t-transparent rounded-full animate-spin mx-auto"></div>
        <p className="text-sm text-slate-500 mt-3">Loading rental statistics...</p>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-10">
      
      {/* Welcome Banner */}
      <div className="bg-gradient-to-r from-blue-700 via-blue-600 to-indigo-700 rounded-3xl p-8 sm:p-10 text-white shadow-xl flex flex-col sm:flex-row sm:items-center justify-between gap-6">
        <div>
          <span className="px-3 py-1 bg-white/20 backdrop-blur rounded-full text-xs font-semibold uppercase tracking-wider">
            Customer Dashboard
          </span>
          <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight mt-3">
            Welcome back, {user?.fullName}!
          </h1>
          <p className="text-xs sm:text-sm text-blue-100 mt-1 max-w-xl">
            Track your wedding vehicle reservations, event rental statuses, and celebration transport.
          </p>
        </div>
        <div>
          <Link
            to="/vehicles"
            className="inline-flex items-center gap-2 px-5 py-3 bg-white text-blue-700 hover:bg-blue-50 font-bold rounded-2xl text-xs sm:text-sm shadow transition self-start sm:self-auto"
          >
            <Car className="w-4 h-4" />
            <span>Book Wedding Vehicle</span>
          </Link>
        </div>
      </div>

      {/* KPI Stats Grid */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-5">
        
        <div className="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm">
          <div className="flex items-center justify-between">
            <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">Total Bookings</p>
            <div className="w-9 h-9 rounded-xl bg-blue-50 text-blue-600 flex items-center justify-center">
              <CalendarCheck className="w-5 h-5" />
            </div>
          </div>
          <p className="text-2xl font-extrabold text-slate-900 mt-2">{stats?.totalBookings || 0}</p>
          <p className="text-[11px] text-slate-400 mt-1">Lifetime platform reservations</p>
        </div>

        <div className="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm">
          <div className="flex items-center justify-between">
            <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">Pending Review</p>
            <div className="w-9 h-9 rounded-xl bg-amber-50 text-amber-600 flex items-center justify-center">
              <Clock className="w-5 h-5" />
            </div>
          </div>
          <p className="text-2xl font-extrabold text-slate-900 mt-2">{stats?.pendingBookings || 0}</p>
          <p className="text-[11px] text-slate-400 mt-1">Awaiting admin confirmation</p>
        </div>

        <div className="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm">
          <div className="flex items-center justify-between">
            <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">Active Rentals</p>
            <div className="w-9 h-9 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center">
              <TrendingUp className="w-5 h-5" />
            </div>
          </div>
          <p className="text-2xl font-extrabold text-slate-900 mt-2">{stats?.activeRentals || 0}</p>
          <p className="text-[11px] text-slate-400 mt-1">Currently on the road</p>
        </div>

        <div className="bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm">
          <div className="flex items-center justify-between">
            <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">Completed Trips</p>
            <div className="w-9 h-9 rounded-xl bg-purple-50 text-purple-600 flex items-center justify-center">
              <CheckCircle2 className="w-5 h-5" />
            </div>
          </div>
          <p className="text-2xl font-extrabold text-slate-900 mt-2">{stats?.completedBookings || 0}</p>
          <p className="text-[11px] text-slate-400 mt-1">Successfully returned</p>
        </div>

      </div>

      {/* Recent Bookings Section */}
      <div className="bg-white rounded-3xl border border-slate-200/80 shadow-sm overflow-hidden">
        <div className="p-6 border-b border-slate-100 flex items-center justify-between">
          <div>
            <h3 className="text-lg font-bold text-slate-900">Recent Booking Requests</h3>
            <p className="text-xs text-slate-500 mt-0.5">Your most recent vehicle rental orders</p>
          </div>
          <Link
            to="/my-bookings"
            className="text-xs font-bold text-blue-600 hover:text-blue-700 flex items-center gap-1"
          >
            <span>View All</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </Link>
        </div>

        {(!stats?.recentBookings || stats.recentBookings.length === 0) ? (
          <div className="p-12 text-center text-slate-500">
            <Car className="w-10 h-10 text-slate-300 mx-auto mb-2" />
            <p className="text-sm font-semibold">No bookings found yet.</p>
            <Link to="/vehicles" className="inline-block mt-3 px-4 py-2 bg-blue-600 text-white rounded-xl text-xs font-bold">
              Browse Vehicles
            </Link>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead className="bg-slate-50 text-slate-500 font-bold uppercase tracking-wider border-b border-slate-100">
                <tr>
                  <th className="px-6 py-3.5">Reference</th>
                  <th className="px-6 py-3.5">Vehicle</th>
                  <th className="px-6 py-3.5">Rental Dates</th>
                  <th className="px-6 py-3.5">Pickup Location</th>
                  <th className="px-6 py-3.5">Amount</th>
                  <th className="px-6 py-3.5">Status</th>
                  <th className="px-6 py-3.5 text-right">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {stats.recentBookings.map((b) => (
                  <tr key={b.id} className="hover:bg-slate-50/80 transition">
                    <td className="px-6 py-4 font-mono font-bold text-slate-900">
                      {b.bookingReference}
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
                          <p className="text-[11px] text-slate-400">{b.vehicleBrand}</p>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-slate-600">
                      <p className="font-medium">{b.pickupDate} → {b.returnDate}</p>
                      <p className="text-[11px] text-slate-400">{b.numberOfDays} days</p>
                    </td>
                    <td className="px-6 py-4 text-slate-600">
                      <div className="flex items-center gap-1 truncate max-w-[150px]">
                        <MapPin className="w-3 h-3 text-slate-400 shrink-0" />
                        <span className="truncate">{b.pickupLocation}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4 font-extrabold text-slate-900 text-sm whitespace-nowrap">
                      ₹ {Number(b.totalAmount).toLocaleString('en-IN')}
                    </td>
                    <td className="px-6 py-4">
                      <StatusBadge status={b.status} />
                    </td>
                    <td className="px-6 py-4 text-right">
                      <Link
                        to="/my-bookings"
                        className="text-xs font-bold text-blue-600 hover:text-blue-700"
                      >
                        Manage
                      </Link>
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
