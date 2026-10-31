import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api';
import StatusBadge, { PaymentStatusBadge } from '../components/StatusBadge';
import { loadRazorpayScript } from '../services/razorpay';
import { 
  CalendarCheck, 
  MapPin, 
  Calendar, 
  Car, 
  X, 
  AlertCircle, 
  CheckCircle, 
  Clock,
  CreditCard,
  FileText,
  Star,
  Receipt,
  Download,
  ShieldCheck,
  Sparkles
} from 'lucide-react';

export default function MyBookingsPage() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('ALL');
  const [cancelModalBooking, setCancelModalBooking] = useState(null);
  const [cancelLoading, setCancelLoading] = useState(false);
  const [notification, setNotification] = useState(null);

  // Payment states
  const [payModalBooking, setPayModalBooking] = useState(null);
  const [payLoading, setPayLoading] = useState(false);
  const [orderData, setOrderData] = useState(null);

  // Review states
  const [reviewModalBooking, setReviewModalBooking] = useState(null);
  const [reviewRating, setReviewRating] = useState(5);
  const [reviewComment, setReviewComment] = useState('');
  const [reviewLoading, setReviewLoading] = useState(false);

  // Invoice state
  const [invoiceDownloadingId, setInvoiceDownloadingId] = useState(null);

  // Payment History state
  const [showHistoryModal, setShowHistoryModal] = useState(false);
  const [paymentHistory, setPaymentHistory] = useState([]);
  const [historyLoading, setHistoryLoading] = useState(false);

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

  // Initiate Razorpay Payment
  const initiatePayment = async (booking) => {
    setPayModalBooking(booking);
    setPayLoading(true);
    try {
      const res = await api.post('/payments/create-order', {
        bookingId: booking.id,
      });
      setOrderData(res.data);

      // Try loading Razorpay Checkout SDK
      const sdkReady = await loadRazorpayScript();
      if (sdkReady && window.Razorpay && !res.data.orderId.startsWith('order_test_')) {
        const options = {
          key: res.data.keyId || res.data.razorpayKeyId,
          amount: res.data.amountInPaise || res.data.amount,
          currency: res.data.currency || 'INR',
          name: 'VehicleSphere Luxury Rentals',
          description: `Booking #${res.data.bookingReference}`,
          image: '/images/vehicles/defender.jpg',
          order_id: res.data.orderId,
          prefill: {
            name: res.data.customerName || '',
            email: res.data.customerEmail || '',
            contact: res.data.customerPhone || '',
          },
          handler: async function (response) {
            await verifyPayment(
              booking.id,
              response.razorpay_order_id,
              response.razorpay_payment_id,
              response.razorpay_signature
            );
          },
          theme: {
            color: '#2563eb',
          },
        };
        const rzpInstance = new window.Razorpay(options);
        rzpInstance.on('payment.failed', function (resp) {
          setNotification({
            type: 'error',
            message: `Payment failed: ${resp.error?.description || 'Transaction declined'}`,
          });
          setPayModalBooking(null);
        });
        rzpInstance.open();
      }
    } catch (err) {
      setNotification({
        type: 'error',
        message: err.response?.data?.message || 'Unable to initiate payment.',
      });
      setPayModalBooking(null);
    } finally {
      setPayLoading(false);
    }
  };

  // Verify Payment on Backend
  const verifyPayment = async (bookingId, orderId, paymentId, signature) => {
    setPayLoading(true);
    try {
      await api.post('/payments/verify', {
        bookingId,
        razorpayOrderId: orderId,
        razorpayPaymentId: paymentId,
        razorpaySignature: signature,
      });
      setNotification({
        type: 'success',
        message: `Payment verified successfully! Your booking is now confirmed & paid.`,
      });
      setPayModalBooking(null);
      setOrderData(null);
      fetchBookings();
    } catch (err) {
      setNotification({
        type: 'error',
        message: err.response?.data?.message || 'Payment signature verification failed.',
      });
    } finally {
      setPayLoading(false);
    }
  };

  // Download PDF Invoice
  const handleDownloadInvoice = async (booking) => {
    setInvoiceDownloadingId(booking.id);
    try {
      const response = await api.get(`/bookings/${booking.id}/invoice`, {
        responseType: 'blob',
      });
      const blob = new Blob([response.data], { type: 'application/pdf' });
      const downloadUrl = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = downloadUrl;
      link.download = `VehicleSphere-Invoice-${booking.bookingReference}.pdf`;
      document.body.appendChild(link);
      link.click();
      setTimeout(() => {
        link.remove();
        window.URL.revokeObjectURL(downloadUrl);
      }, 3000);
      setNotification({
        type: 'success',
        message: `Tax invoice for booking ${booking.bookingReference} downloaded successfully.`,
      });
    } catch (err) {
      setNotification({
        type: 'error',
        message: 'Failed to generate tax invoice PDF. Please try again.',
      });
    } finally {
      setInvoiceDownloadingId(null);
    }
  };

  // Submit Review
  const handleSubmitReview = async (e) => {
    e.preventDefault();
    if (!reviewModalBooking) return;
    setReviewLoading(true);
    try {
      await api.post('/reviews', {
        bookingId: reviewModalBooking.id,
        rating: reviewRating,
        comment: reviewComment.trim(),
      });
      setNotification({
        type: 'success',
        message: 'Thank you! Your wedding vehicle review has been submitted.',
      });
      setReviewModalBooking(null);
      setReviewComment('');
      setReviewRating(5);
    } catch (err) {
      setNotification({
        type: 'error',
        message: err.response?.data?.message || 'Failed to submit review.',
      });
    } finally {
      setReviewLoading(false);
    }
  };

  // Open Payment History
  const openPaymentHistory = async () => {
    setShowHistoryModal(true);
    setHistoryLoading(true);
    try {
      const res = await api.get('/payments/my-payments');
      setPaymentHistory(res.data);
    } catch (err) {
      console.error('Failed to load payment history', err);
    } finally {
      setHistoryLoading(false);
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
            Review your celebration reservations, download official GST tax invoices, and complete secure payments.
          </p>
        </div>
        <div className="flex items-center gap-3 self-start sm:self-auto">
          <button
            onClick={openPaymentHistory}
            className="px-4 py-2.5 bg-white border border-slate-200 text-slate-700 hover:bg-slate-50 font-bold rounded-xl text-xs shadow-sm flex items-center gap-2 transition"
          >
            <Receipt className="w-4 h-4 text-blue-600" />
            <span>Payment History</span>
          </button>
          <Link
            to="/vehicles"
            className="px-5 py-2.5 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-xl text-xs shadow-sm"
          >
            Book Another Vehicle
          </Link>
        </div>
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
          <p className="text-xs text-slate-500 mt-1">Ready to plan your wedding fleet? Discover available vehicles right now.</p>
          <Link
            to="/vehicles"
            className="inline-block mt-4 px-4 py-2 bg-blue-600 text-white rounded-xl text-xs font-bold shadow"
          >
            Browse Available Vehicles
          </Link>
        </div>
      ) : (
        <div className="space-y-4">
          {filteredBookings.map((b) => {
            const isPaid = b.paymentStatus === 'PAID';
            const canPay = !isPaid && b.status !== 'CANCELLED' && b.status !== 'REJECTED';
            const canReview = b.status === 'COMPLETED';
            const canCancel = (b.status === 'PENDING' || b.status === 'CONFIRMED');

            return (
              <div
                key={b.id}
                className="bg-white rounded-2xl border border-slate-200/80 p-5 sm:p-6 shadow-sm hover:shadow transition flex flex-col lg:flex-row lg:items-center justify-between gap-6"
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
                    <div className="flex flex-wrap items-center gap-2">
                      <span className="font-mono text-xs font-bold text-blue-600">{b.bookingReference}</span>
                      <StatusBadge status={b.status} />
                      <PaymentStatusBadge status={b.paymentStatus} />
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
                      <p className="text-[11px] text-slate-400 italic pt-0.5">
                        Note: {b.notes}
                      </p>
                    )}
                  </div>
                </div>

                {/* Right: Pricing & Action Controls */}
                <div className="flex flex-col sm:flex-row lg:flex-col items-start sm:items-center lg:items-end justify-between border-t lg:border-t-0 pt-4 lg:pt-0 border-slate-100 gap-4">
                  <div className="text-left lg:text-right">
                    <p className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Total Rental Charge</p>
                    <p className="text-xl sm:text-2xl font-black text-slate-900">₹ {Number(b.totalAmount).toLocaleString('en-IN')}</p>
                    <p className="text-[11px] text-slate-400">₹ {Number(b.pricePerDay).toLocaleString('en-IN')} × {b.numberOfDays} days</p>
                  </div>

                  {/* Actions Bar */}
                  <div className="flex flex-wrap items-center gap-2">
                    {/* Pay Now Button */}
                    {canPay && (
                      <button
                        onClick={() => initiatePayment(b)}
                        className="px-3.5 py-1.5 rounded-xl bg-blue-600 hover:bg-blue-700 text-white text-xs font-bold transition flex items-center gap-1.5 shadow-sm shadow-blue-500/20"
                      >
                        <CreditCard className="w-3.5 h-3.5" />
                        <span>Pay with Razorpay</span>
                      </button>
                    )}

                    {/* Tax Invoice Download Button */}
                    {b.status !== 'REJECTED' && (
                      <button
                        onClick={() => handleDownloadInvoice(b)}
                        disabled={invoiceDownloadingId === b.id}
                        title="Download GST Rental Tax Invoice PDF"
                        className="px-3 py-1.5 rounded-xl border border-slate-200 bg-slate-50 hover:bg-slate-100 text-slate-700 text-xs font-bold transition flex items-center gap-1.5 shadow-sm disabled:opacity-50"
                      >
                        <Download className="w-3.5 h-3.5 text-blue-600" />
                        <span>{invoiceDownloadingId === b.id ? 'Generating PDF...' : 'Tax Invoice'}</span>
                      </button>
                    )}

                    {/* Rate & Review Button for Completed Trips */}
                    {canReview && (
                      <button
                        onClick={() => {
                          setReviewModalBooking(b);
                          setReviewRating(5);
                          setReviewComment('');
                        }}
                        className="px-3 py-1.5 rounded-xl border border-amber-200 bg-amber-50 hover:bg-amber-100 text-amber-800 text-xs font-bold transition flex items-center gap-1.5"
                      >
                        <Star className="w-3.5 h-3.5 fill-amber-500 text-amber-500" />
                        <span>Review Vehicle</span>
                      </button>
                    )}

                    {/* Cancel Booking */}
                    {canCancel && (
                      <button
                        onClick={() => setCancelModalBooking(b)}
                        className="px-3 py-1.5 rounded-xl border border-rose-200 text-rose-600 hover:bg-rose-50 text-xs font-bold transition"
                      >
                        Cancel
                      </button>
                    )}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}

      {/* Razorpay Test Modal (For Test / Simulation Mode) */}
      {payModalBooking && orderData && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-sm">
          <div className="bg-white rounded-3xl max-w-md w-full p-6 shadow-2xl border border-slate-100 space-y-5 animate-in fade-in duration-200">
            <div className="flex items-center justify-between pb-3 border-b border-slate-100">
              <div className="flex items-center gap-2">
                <div className="w-8 h-8 rounded-lg bg-blue-600 text-white flex items-center justify-center font-bold text-xs">
                  RZP
                </div>
                <div>
                  <h3 className="text-sm font-bold text-slate-900">Razorpay Test Gateway</h3>
                  <p className="text-[11px] text-slate-400">Sandbox Test Environment</p>
                </div>
              </div>
              <button
                onClick={() => setPayModalBooking(null)}
                className="p-1 rounded-lg hover:bg-slate-100 text-slate-400 hover:text-slate-600"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            <div className="p-4 rounded-2xl bg-slate-50 border border-slate-200/80 space-y-2 text-xs">
              <div className="flex justify-between text-slate-600">
                <span>Vehicle:</span>
                <span className="font-bold text-slate-900">{payModalBooking.vehicleName}</span>
              </div>
              <div className="flex justify-between text-slate-600">
                <span>Booking Ref:</span>
                <span className="font-mono font-bold text-blue-600">{payModalBooking.bookingReference}</span>
              </div>
              <div className="flex justify-between text-slate-600">
                <span>Order ID:</span>
                <span className="font-mono text-slate-700">{orderData.orderId}</span>
              </div>
              <div className="pt-2 border-t border-slate-200 flex justify-between text-sm font-bold text-slate-900">
                <span>Payable Amount:</span>
                <span className="text-blue-600 font-extrabold text-base">₹ {Number(payModalBooking.totalAmount).toLocaleString('en-IN')}</span>
              </div>
            </div>

            <div className="p-3 bg-amber-50 rounded-xl border border-amber-200 text-[11px] text-amber-800 flex items-start gap-2">
              <ShieldCheck className="w-4 h-4 text-amber-600 shrink-0 mt-0.5" />
              <span>
                Test Mode is active. No real credit card or bank funds are charged. Clicking below verifies HMAC signature and confirms booking on the backend.
              </span>
            </div>

            <div className="space-y-2 pt-2">
              <button
                type="button"
                disabled={payLoading}
                onClick={() => {
                  const fakePaymentId = 'pay_test_' + Date.now().toString().slice(-8);
                  verifyPayment(
                    payModalBooking.id,
                    orderData.orderId,
                    fakePaymentId,
                    'mock_sig_' + fakePaymentId
                  );
                }}
                className="w-full py-2.5 px-4 bg-emerald-600 hover:bg-emerald-700 text-white font-bold rounded-xl text-xs transition shadow-md shadow-emerald-500/20 flex items-center justify-center gap-1.5"
              >
                {payLoading ? (
                  <span className="animate-spin w-4 h-4 border-2 border-white border-t-transparent rounded-full"></span>
                ) : (
                  <>
                    <CreditCard className="w-4 h-4" />
                    <span>Simulate Payment Success (Authorize ₹ {Number(payModalBooking.totalAmount).toLocaleString('en-IN')})</span>
                  </>
                )}
              </button>

              <div className="grid grid-cols-2 gap-2">
                <button
                  type="button"
                  disabled={payLoading}
                  onClick={() => {
                    setNotification({
                      type: 'error',
                      message: 'Payment simulation declined: Issuing bank declined the test transaction (ERROR: INSUFFICIENT_FUNDS_OR_LIMIT).',
                    });
                    setPayModalBooking(null);
                    setOrderData(null);
                  }}
                  className="py-2 px-3 bg-rose-50 hover:bg-rose-100 text-rose-700 font-bold rounded-xl text-xs border border-rose-200 transition text-center"
                >
                  Simulate Bank Failure
                </button>
                <button
                  type="button"
                  disabled={payLoading}
                  onClick={() => {
                    setNotification({
                      type: 'error',
                      message: 'Payment was cancelled by the user. Reservation remains pending payment.',
                    });
                    setPayModalBooking(null);
                    setOrderData(null);
                  }}
                  className="py-2 px-3 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold rounded-xl text-xs transition text-center"
                >
                  Cancel Payment
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Review Submission Modal */}
      {reviewModalBooking && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-sm">
          <div className="bg-white rounded-3xl max-w-md w-full p-6 shadow-2xl border border-slate-100 space-y-4">
            <div className="flex items-center justify-between pb-2 border-b border-slate-100">
              <div className="flex items-center gap-2">
                <div className="w-8 h-8 rounded-lg bg-amber-100 text-amber-600 flex items-center justify-center font-bold">
                  <Star className="w-4 h-4 fill-amber-500" />
                </div>
                <div>
                  <h3 className="text-sm font-bold text-slate-900">Rate Your Wedding Ride</h3>
                  <p className="text-[11px] text-slate-400">{reviewModalBooking.vehicleName}</p>
                </div>
              </div>
              <button
                onClick={() => setReviewModalBooking(null)}
                className="p-1 rounded-lg hover:bg-slate-100 text-slate-400 hover:text-slate-600"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            <form onSubmit={handleSubmitReview} className="space-y-4">
              {/* Star Rating Selector */}
              <div>
                <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-2">
                  Overall Rating
                </label>
                <div className="flex items-center gap-2">
                  {[1, 2, 3, 4, 5].map((star) => (
                    <button
                      key={star}
                      type="button"
                      onClick={() => setReviewRating(star)}
                      className="p-1 hover:scale-110 transition"
                    >
                      <Star
                        className={`w-7 h-7 ${
                          star <= reviewRating
                            ? 'text-amber-500 fill-amber-500'
                            : 'text-slate-200'
                        }`}
                      />
                    </button>
                  ))}
                  <span className="text-xs font-bold text-amber-600 ml-2">
                    {reviewRating} of 5 Stars
                  </span>
                </div>
              </div>

              {/* Review Comment */}
              <div>
                <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">
                  Your Feedback & Celebration Experience
                </label>
                <textarea
                  rows={4}
                  required
                  value={reviewComment}
                  onChange={(e) => setReviewComment(e.target.value)}
                  placeholder="Share details about the car condition, driver courtesy, punctuality, and wedding entry experience..."
                  className="w-full px-3 py-2 text-xs font-medium rounded-xl border border-slate-200 focus:ring-2 focus:ring-blue-500 bg-slate-50"
                />
              </div>

              <div className="grid grid-cols-2 gap-3 pt-2">
                <button
                  type="button"
                  onClick={() => setReviewModalBooking(null)}
                  className="py-2.5 px-4 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold rounded-xl text-xs transition"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={reviewLoading}
                  className="py-2.5 px-4 bg-amber-500 hover:bg-amber-600 text-white font-bold rounded-xl text-xs transition shadow-md shadow-amber-500/20 flex items-center justify-center gap-1.5"
                >
                  {reviewLoading ? 'Submitting...' : 'Post Review'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Payment History Modal */}
      {showHistoryModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-sm">
          <div className="bg-white rounded-3xl max-w-2xl w-full p-6 shadow-2xl border border-slate-100 space-y-4 max-h-[85vh] flex flex-col">
            <div className="flex items-center justify-between pb-3 border-b border-slate-100">
              <div className="flex items-center gap-2">
                <Receipt className="w-5 h-5 text-blue-600" />
                <h3 className="text-base font-bold text-slate-900">Payment & Transaction Receipts</h3>
              </div>
              <button
                onClick={() => setShowHistoryModal(false)}
                className="p-1 rounded-lg hover:bg-slate-100 text-slate-400 hover:text-slate-600"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            <div className="overflow-y-auto flex-1 space-y-3 pr-1">
              {historyLoading ? (
                <div className="py-12 text-center">
                  <div className="w-8 h-8 border-3 border-blue-600 border-t-transparent rounded-full animate-spin mx-auto"></div>
                  <p className="text-xs text-slate-500 mt-2">Loading transactions...</p>
                </div>
              ) : paymentHistory.length === 0 ? (
                <div className="text-center py-12 text-slate-400">
                  <CreditCard className="w-10 h-10 mx-auto mb-2 opacity-40" />
                  <p className="text-xs font-semibold">No payment records found yet.</p>
                </div>
              ) : (
                paymentHistory.map((p) => (
                  <div
                    key={p.id}
                    className="p-4 rounded-2xl border border-slate-200/80 bg-slate-50/50 flex flex-col sm:flex-row sm:items-center justify-between gap-3"
                  >
                    <div>
                      <div className="flex items-center gap-2">
                        <span className="font-mono text-xs font-bold text-slate-900">
                          {p.razorpayPaymentId || `TXN-${p.id}`}
                        </span>
                        <PaymentStatusBadge status={p.status} />
                      </div>
                      <p className="text-xs text-slate-500 mt-1">
                        Order ID: <span className="font-mono text-slate-700">{p.razorpayOrderId}</span>
                      </p>
                      <p className="text-[11px] text-slate-400 mt-0.5">
                        {p.createdAt ? new Date(p.createdAt).toLocaleString('en-IN') : 'Recent'} • Method: {p.paymentMethod || 'Razorpay Online'}
                      </p>
                    </div>
                    <div className="text-right">
                      <span className="text-base font-black text-slate-900">
                        ₹ {Number(p.amount).toLocaleString('en-IN')}
                      </span>
                    </div>
                  </div>
                ))
              )}
            </div>

            <div className="pt-2 border-t border-slate-100 flex justify-end">
              <button
                onClick={() => setShowHistoryModal(false)}
                className="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 text-xs font-bold rounded-xl transition"
              >
                Close
              </button>
            </div>
          </div>
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
