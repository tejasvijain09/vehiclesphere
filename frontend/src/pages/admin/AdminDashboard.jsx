import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../../services/api';
import StatusBadge, { PaymentStatusBadge } from '../../components/StatusBadge';
import { 
  Users, 
  Car, 
  CalendarCheck, 
  Clock, 
  CheckCircle2, 
  TrendingUp, 
  ShieldCheck, 
  ArrowRight,
  Plus,
  BarChart3,
  PieChart,
  Award,
  IndianRupee,
  Activity
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
        <p className="text-sm text-slate-500 mt-3">Loading administrative metrics & analytics...</p>
      </div>
    );
  }

  // Max revenue for bar normalization
  const maxMonthlyRevenue = Math.max(
    ...(stats?.monthlyTrends?.map((m) => Number(m.revenue)) || [1]),
    1
  );

  const maxMonthlyBookings = Math.max(
    ...(stats?.monthlyTrends?.map((m) => m.bookingCount) || [1]),
    1
  );

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-10">
      
      {/* Top Banner */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <div className="flex items-center gap-2">
            <span className="px-2.5 py-0.5 rounded-md bg-blue-100 text-blue-800 text-[10px] font-extrabold uppercase tracking-wider">
              Control Panel & Analytics
            </span>
            <span className="flex items-center gap-1 text-[11px] text-emerald-600 font-bold">
              <span className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
              Live Operations
            </span>
          </div>
          <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight mt-1">
            Platform Administration
          </h1>
          <p className="text-sm text-slate-500">
            Real-time fleet operations, payment reconciliation, and wedding booking analytics.
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
        
        <div className="bg-white p-6 rounded-3xl border border-slate-200/80 shadow-sm hover:shadow-md transition">
          <div className="flex items-center justify-between">
            <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">Paid Revenue</p>
            <div className="w-10 h-10 rounded-2xl bg-emerald-50 text-emerald-600 flex items-center justify-center font-bold">
              <IndianRupee className="w-5 h-5" />
            </div>
          </div>
          <p className="text-2xl sm:text-3xl font-black text-slate-900 mt-2">
            ₹ {Number(stats?.totalRevenue || 0).toLocaleString('en-IN')}
          </p>
          <p className="text-[11px] text-emerald-600 font-semibold mt-1">
            Verified Gateway Collections
          </p>
        </div>

        <div className="bg-white p-6 rounded-3xl border border-slate-200/80 shadow-sm hover:shadow-md transition">
          <div className="flex items-center justify-between">
            <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">Total Fleet</p>
            <div className="w-10 h-10 rounded-2xl bg-blue-50 text-blue-600 flex items-center justify-center">
              <Car className="w-5 h-5" />
            </div>
          </div>
          <p className="text-3xl font-black text-slate-900 mt-2">{stats?.totalVehicles || 0}</p>
          <p className="text-[11px] text-slate-500 mt-1">{stats?.availableVehicles || 0} currently available</p>
        </div>

        <div className="bg-white p-6 rounded-3xl border border-slate-200/80 shadow-sm hover:shadow-md transition">
          <div className="flex items-center justify-between">
            <p className="text-xs font-bold text-slate-400 uppercase tracking-wider">Total Bookings</p>
            <div className="w-10 h-10 rounded-2xl bg-purple-50 text-purple-600 flex items-center justify-center">
              <CalendarCheck className="w-5 h-5" />
            </div>
          </div>
          <p className="text-3xl font-black text-slate-900 mt-2">{stats?.totalBookings || 0}</p>
          <p className="text-[11px] text-slate-500 mt-1">{stats?.pendingBookings || 0} pending review</p>
        </div>

        <div className="bg-white p-6 rounded-3xl border border-slate-200/80 shadow-sm hover:shadow-md transition">
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

      {/* Analytics Charts & Status Distribution */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        
        {/* Monthly Revenue & Booking Trends */}
        <div className="bg-white p-6 sm:p-7 rounded-3xl border border-slate-200/80 shadow-sm space-y-6">
          <div className="flex items-center justify-between">
            <div>
              <div className="flex items-center gap-2">
                <BarChart3 className="w-5 h-5 text-blue-600" />
                <h3 className="text-base font-bold text-slate-900">Monthly Revenue & Trend</h3>
              </div>
              <p className="text-xs text-slate-400 mt-0.5">Verified paid bookings over time (₹ in thousands)</p>
            </div>
          </div>

          {(!stats?.monthlyTrends || stats.monthlyTrends.length === 0) ? (
            <div className="py-12 text-center text-xs text-slate-400">
              No monthly trend data recorded yet.
            </div>
          ) : (
            <div className="space-y-4 pt-2">
              <div className="h-48 flex items-end gap-3 sm:gap-6 pt-6 border-b border-slate-100 pb-2">
                {stats.monthlyTrends.map((item, idx) => {
                  const revHeight = Math.max(
                    (Number(item.revenue) / maxMonthlyRevenue) * 100,
                    item.revenue > 0 ? 8 : 4
                  );
                  return (
                    <div key={idx} className="flex-1 flex flex-col items-center gap-2 h-full justify-end group">
                      <div className="text-[10px] font-bold text-slate-600 opacity-0 group-hover:opacity-100 transition whitespace-nowrap">
                        ₹ {Number(item.revenue).toLocaleString('en-IN')}
                      </div>
                      <div className="w-full max-w-[48px] bg-slate-100 rounded-xl overflow-hidden flex flex-col justify-end h-36">
                        <div
                          className="w-full bg-gradient-to-t from-blue-700 to-blue-500 rounded-xl transition-all duration-700 group-hover:brightness-110"
                          style={{ height: `${revHeight}%` }}
                        ></div>
                      </div>
                      <span className="text-[10px] font-bold text-slate-500">
                        {item.month}
                      </span>
                    </div>
                  );
                })}
              </div>

              <div className="flex items-center justify-between text-xs text-slate-500 pt-1">
                <span className="flex items-center gap-2">
                  <span className="w-3 h-3 rounded-full bg-blue-600 inline-block"></span>
                  <span>Paid Rental Revenue</span>
                </span>
                <span className="text-[11px] text-slate-400">
                  Aggregate: ₹ {Number(stats?.totalRevenue || 0).toLocaleString('en-IN')}
                </span>
              </div>
            </div>
          )}
        </div>

        {/* Booking Status Distribution */}
        <div className="bg-white p-6 sm:p-7 rounded-3xl border border-slate-200/80 shadow-sm space-y-6">
          <div className="flex items-center justify-between">
            <div>
              <div className="flex items-center gap-2">
                <PieChart className="w-5 h-5 text-purple-600" />
                <h3 className="text-base font-bold text-slate-900">Booking Status Breakdown</h3>
              </div>
              <p className="text-xs text-slate-400 mt-0.5">Live distribution of reservations across states</p>
            </div>
          </div>

          <div className="space-y-3 pt-2">
            {[
              { label: 'CONFIRMED', count: stats?.confirmedBookings || 0, color: 'bg-blue-600', text: 'text-blue-700', bg: 'bg-blue-50' },
              { label: 'PENDING', count: stats?.pendingBookings || 0, color: 'bg-amber-500', text: 'text-amber-700', bg: 'bg-amber-50' },
              { label: 'ACTIVE', count: stats?.activeRentals || 0, color: 'bg-emerald-600', text: 'text-emerald-700', bg: 'bg-emerald-50' },
              { label: 'COMPLETED', count: stats?.completedBookings || 0, color: 'bg-purple-600', text: 'text-purple-700', bg: 'bg-purple-50' },
              { label: 'CANCELLED', count: stats?.cancelledBookings || 0, color: 'bg-slate-400', text: 'text-slate-700', bg: 'bg-slate-100' },
            ].map((item) => {
              const total = stats?.totalBookings || 1;
              const percent = Math.round((item.count / total) * 100);
              return (
                <div key={item.label} className="space-y-1">
                  <div className="flex items-center justify-between text-xs">
                    <span className="font-bold text-slate-700 flex items-center gap-2">
                      <span className={`w-2.5 h-2.5 rounded-full ${item.color}`}></span>
                      {item.label}
                    </span>
                    <span className="font-mono text-slate-500">
                      {item.count} ({percent}%)
                    </span>
                  </div>
                  <div className="h-2 w-full bg-slate-100 rounded-full overflow-hidden">
                    <div
                      className={`h-full ${item.color} rounded-full transition-all duration-700`}
                      style={{ width: `${percent}%` }}
                    ></div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

      </div>

      {/* Top 5 Most Rented Wedding Vehicles */}
      {stats?.topRentedVehicles && stats.topRentedVehicles.length > 0 && (
        <div className="bg-white p-6 sm:p-7 rounded-3xl border border-slate-200/80 shadow-sm space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-slate-100">
            <div className="flex items-center gap-2">
              <Award className="w-5 h-5 text-amber-500" />
              <div>
                <h3 className="text-base font-bold text-slate-900">Top Rented Wedding Vehicles</h3>
                <p className="text-xs text-slate-400">Most preferred wedding entry and fleet choices</p>
              </div>
            </div>
            <Link to="/admin/vehicles" className="text-xs font-bold text-blue-600 hover:text-blue-700">
              Manage Fleet
            </Link>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {stats.topRentedVehicles.map((top, idx) => (
              <div
                key={top.vehicleId}
                className="p-4 rounded-2xl border border-slate-200/80 bg-slate-50/50 flex items-center gap-4 relative overflow-hidden"
              >
                <div className="w-8 h-8 rounded-full bg-slate-900 text-white text-xs font-black flex items-center justify-center shrink-0">
                  #{idx + 1}
                </div>
                <div className="flex-1 min-w-0">
                  <h4 className="text-xs font-bold text-slate-900 truncate">{top.vehicleName}</h4>
                  <p className="text-[11px] text-slate-500">{top.categoryName || 'Luxury Fleet'}</p>
                  <p className="text-[11px] font-extrabold text-blue-600 mt-1">
                    {top.bookingCount} reservation{top.bookingCount > 1 ? 's' : ''}
                  </p>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

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
                  <th className="px-6 py-3.5">Status & Payment</th>
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
                      <div className="flex flex-col gap-1 items-start">
                        <StatusBadge status={b.status} />
                        <PaymentStatusBadge status={b.paymentStatus} />
                      </div>
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
