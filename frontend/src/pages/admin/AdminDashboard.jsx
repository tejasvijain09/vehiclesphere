import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../../services/api';
import StatusBadge from '../../components/StatusBadge';
import { 
  Users, 
  Car, 
  CalendarCheck, 
  DollarSign, 
  Clock, 
  CheckCircle2, 
  TrendingUp, 
  ShieldCheck, 
  ArrowRight,
  Plus
} from 'lucide-react';

export default function AdminDashboard() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchStats = async () => {
    try {
      const res = await api.get('/admin/dashboard');
      setStats(res.data);
    } catch (err) {
      console.error('Failed to load admin stats', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStats();
  }, []);

  if (loading) {
    return (
      <div className="py-20 text-center">
        <div className="w-10 h-10 border-4 border-blue-600 border-t-transparent rounded-full animate-spin mx-auto"></div>
        <p className="text-sm text-slate-500 mt-3">Loading administrative metrics...</p>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-10">
      
      {/* Top Banner */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <div className="flex items-center gap-2">
            <span className="px-2.5 py-0.5 rounded-md bg-blue-100 text-blue-800 text-[10px] font-extrabold uppercase tracking-wider">
              Control Panel
            </span>
          </div>
          <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight mt-1">
            Platform Administration
          </h1>
          <p className="text-sm text-slate-500">
            Real-time fleet operations, customer rental statistics, and revenue insights.
          </p>
        </div>

        <div className="flex items-center gap-3">
          <Link
            to="/admin/vehicles"
            className="inline-flex items-center gap-2 px-4 py-2.5 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-xl text-xs shadow-md shadow-blue-500/20 transition"
          >
            <Plus className="w-4 h-4" />
            <span>Manage Fleet</span>
          </Link>
          <Link
            to="/admin/bookings"
            className="inline-flex items-center gap-2 px-4 py-2.5 bg-slate-900 hover:bg-slate-800 text-white font-bold rounded-xl text-xs shadow transition"
          >
            <CalendarCheck className="w-4 h-4" />
            <span>Review Bookings</span>
          </Link>
        </div>
      </div>

      {/* Primary KPI Metrics */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-5">
        
        <div className="bg-white p-6 rounded-3xl border border-slate-200/80 shadow-sm">
          <div className="flex items-center justify-between">
            <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">Total Revenue</p>
            <div className="w-10 h-10 rounded-2xl bg-emerald-50 text-emerald-600 flex items-center justify-center">
              <DollarSign className="w-5 h-5" />
            </div>
          </div>
          <p className="text-2xl sm:text-3xl font-black text-slate-900 mt-2">₹ {Number(stats?.totalRevenue || 0).toLocaleString('en-IN')}</p>
          <p className="text-[11px] text-emerald-600 font-semibold mt-1">Confirmed & Active Rentals</p>
        </div>

        <div className="bg-white p-6 rounded-3xl border border-slate-200/80 shadow-sm">
          <div className="flex items-center justify-between">
            <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">Total Fleet</p>
            <div className="w-10 h-10 rounded-2xl bg-blue-50 text-blue-600 flex items-center justify-center">
              <Car className="w-5 h-5" />
            </div>
          </div>
          <p className="text-3xl font-black text-slate-900 mt-2">{stats?.totalVehicles || 0}</p>
          <p className="text-[11px] text-slate-500 mt-1">{stats?.availableVehicles || 0} currently available</p>
        </div>

        <div className="bg-white p-6 rounded-3xl border border-slate-200/80 shadow-sm">
          <div className="flex items-center justify-between">
            <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">Total Bookings</p>
            <div className="w-10 h-10 rounded-2xl bg-purple-50 text-purple-600 flex items-center justify-center">
              <CalendarCheck className="w-5 h-5" />
            </div>
          </div>
          <p className="text-3xl font-black text-slate-900 mt-2">{stats?.totalBookings || 0}</p>
          <p className="text-[11px] text-slate-500 mt-1">{stats?.pendingBookings || 0} pending review</p>
        </div>

        <div className="bg-white p-6 rounded-3xl border border-slate-200/80 shadow-sm">
          <div className="flex items-center justify-between">
            <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">Registered Users</p>
            <div className="w-10 h-10 rounded-2xl bg-indigo-50 text-indigo-600 flex items-center justify-center">
              <Users className="w-5 h-5" />
            </div>
          </div>
          <p className="text-3xl font-black text-slate-900 mt-2">{stats?.totalCustomers || 0}</p>
          <p className="text-[11px] text-indigo-600 font-semibold mt-1">Verified Customers</p>
        </div>

      </div>

      {/* Secondary Operational Breakdown */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-5">
        <div className="bg-amber-50/70 border border-amber-200/70 p-5 rounded-2xl flex items-center gap-4">
          <div className="w-12 h-12 rounded-xl bg-amber-500 text-white flex items-center justify-center shrink-0">
            <Clock className="w-6 h-6" />
          </div>
          <div>
            <p className="text-2xl font-black text-amber-900">{stats?.pendingBookings || 0}</p>
            <p className="text-xs font-bold text-amber-800">Pending Approval Requests</p>
          </div>
        </div>

        <div className="bg-emerald-50/70 border border-emerald-200/70 p-5 rounded-2xl flex items-center gap-4">
          <div className="w-12 h-12 rounded-xl bg-emerald-600 text-white flex items-center justify-center shrink-0">
            <TrendingUp className="w-6 h-6" />
          </div>
          <div>
            <p className="text-2xl font-black text-emerald-900">{stats?.activeRentals || 0}</p>
            <p className="text-xs font-bold text-emerald-800">Active Live Rentals</p>
          </div>
        </div>

        <div className="bg-blue-50/70 border border-blue-200/70 p-5 rounded-2xl flex items-center gap-4">
          <div className="w-12 h-12 rounded-xl bg-blue-600 text-white flex items-center justify-center shrink-0">
            <CheckCircle2 className="w-6 h-6" />
          </div>
          <div>
            <p className="text-2xl font-black text-blue-900">{stats?.completedBookings || 0}</p>
            <p className="text-xs font-bold text-blue-800">Completed Reservations</p>
          </div>
        </div>
      </div>

      {/* Recent Bookings Queue */}
      <div className="bg-white rounded-3xl border border-slate-200/80 shadow-sm overflow-hidden">
        <div className="p-6 border-b border-slate-100 flex items-center justify-between">
          <div>
            <h3 className="text-lg font-bold text-slate-900">Recent Customer Requests</h3>
            <p className="text-xs text-slate-500 mt-0.5">Most recent booking records submitted across the platform</p>
          </div>
          <Link
            to="/admin/bookings"
            className="text-xs font-bold text-blue-600 hover:text-blue-700 flex items-center gap-1"
          >
            <span>View All Bookings</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </Link>
        </div>

        {(!stats?.recentBookings || stats.recentBookings.length === 0) ? (
          <div className="p-12 text-center text-slate-400 text-xs">
            No booking records found in the database.
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead className="bg-slate-50 text-slate-500 font-bold uppercase tracking-wider border-b border-slate-100">
                <tr>
                  <th className="px-6 py-3.5">Reference</th>
                  <th className="px-6 py-3.5">Customer</th>
                  <th className="px-6 py-3.5">Vehicle</th>
                  <th className="px-6 py-3.5">Dates</th>
                  <th className="px-6 py-3.5">Total Amount</th>
                  <th className="px-6 py-3.5">Status</th>
                  <th className="px-6 py-3.5 text-right">Quick Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {stats.recentBookings.map((b) => (
                  <tr key={b.id} className="hover:bg-slate-50/80 transition">
                    <td className="px-6 py-4 font-mono font-bold text-slate-900">
                      {b.bookingReference}
                    </td>
                    <td className="px-6 py-4">
                      <p className="font-bold text-slate-900">{b.customerName}</p>
                      <p className="text-[11px] text-slate-400">{b.customerEmail}</p>
                    </td>
                    <td className="px-6 py-4">
                      <p className="font-bold text-slate-900">{b.vehicleName}</p>
                      <p className="text-[11px] text-slate-400">{b.vehicleBrand} • {b.vehicleRegistrationNumber}</p>
                    </td>
                    <td className="px-6 py-4 text-slate-600">
                      <p className="font-medium">{b.pickupDate} → {b.returnDate}</p>
                      <p className="text-[11px] text-slate-400">{b.numberOfDays} days</p>
                    </td>
                    <td className="px-6 py-4 font-extrabold text-slate-900 text-sm whitespace-nowrap">
                      ₹ {Number(b.totalAmount).toLocaleString('en-IN')}
                    </td>
                    <td className="px-6 py-4">
                      <StatusBadge status={b.status} />
                    </td>
                    <td className="px-6 py-4 text-right">
                      <Link
                        to="/admin/bookings"
                        className="text-xs font-bold text-blue-600 hover:text-blue-700"
                      >
                        Review
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
