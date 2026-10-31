import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Fuel, Users, Gauge, MapPin, ArrowRight, Heart } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';

export default function VehicleCard({ vehicle, initialWishlisted = false, onWishlistToggle }) {
  const formattedPrice = Number(vehicle.pricePerDay).toLocaleString('en-IN');
  const { user } = useAuth();
  const navigate = useNavigate();
  const [isWishlisted, setIsWishlisted] = useState(initialWishlisted);
  const [wishlistLoading, setWishlistLoading] = useState(false);

  const handleToggleWishlist = async (e) => {
    e.preventDefault();
    e.stopPropagation();

    if (!user) {
      navigate('/login');
      return;
    }

    setWishlistLoading(true);
    try {
      if (isWishlisted) {
        await api.delete(`/wishlist/${vehicle.id}`);
        setIsWishlisted(false);
        if (onWishlistToggle) onWishlistToggle(vehicle.id, false);
      } else {
        await api.post(`/wishlist/${vehicle.id}`);
        setIsWishlisted(true);
        if (onWishlistToggle) onWishlistToggle(vehicle.id, true);
      }
    } catch (err) {
      console.error('Wishlist toggle error', err);
    } finally {
      setWishlistLoading(false);
    }
  };

  return (
    <div className="bg-white rounded-2xl border border-slate-200/80 overflow-hidden shadow-sm hover:shadow-md transition-all duration-300 flex flex-col group relative">
      
      {/* Image container */}
      <div className="relative h-48 sm:h-52 w-full overflow-hidden bg-slate-100">
        <img
          src={vehicle.imageUrl}
          alt={vehicle.name}
          className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
          onError={(e) => {
            e.target.src = 'https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=800&q=80';
          }}
        />
        <div className="absolute top-3 left-3 flex gap-2">
          <span className="px-2.5 py-1 text-xs font-bold rounded-lg bg-white/95 backdrop-blur text-slate-800 shadow-sm">
            {vehicle.categoryName || 'Wedding Fleet'}
          </span>
          {!vehicle.available && (
            <span className="px-2.5 py-1 text-xs font-bold rounded-lg bg-rose-600 text-white shadow-sm">
              Booked
            </span>
          )}
        </div>

        {/* Wishlist Heart Button */}
        <button
          type="button"
          onClick={handleToggleWishlist}
          disabled={wishlistLoading}
          aria-label={isWishlisted ? "Remove from wishlist" : "Add to wishlist"}
          className={`absolute top-3 right-3 w-9 h-9 rounded-full flex items-center justify-center transition backdrop-blur shadow-md ${
            isWishlisted
              ? 'bg-rose-500 text-white'
              : 'bg-white/90 text-slate-600 hover:text-rose-500 hover:bg-white'
          }`}
        >
          <Heart className={`w-4 h-4 ${isWishlisted ? 'fill-current' : ''}`} />
        </button>

        <div className="absolute bottom-3 right-3">
          <div className="px-3 py-1.5 rounded-xl bg-slate-900/85 backdrop-blur text-white flex items-baseline gap-1.5 shadow-sm">
            <span className="text-sm sm:text-base font-extrabold tracking-tight">₹ {formattedPrice}</span>
            <span className="text-[11px] text-slate-300 font-medium">/ day</span>
          </div>
        </div>
      </div>

      {/* Content */}
      <div className="p-5 flex-1 flex flex-col justify-between">
        <div>
          <div className="flex items-center justify-between text-xs font-semibold text-blue-600 mb-1 uppercase tracking-wider">
            <span>{vehicle.brand}</span>
            <span>{vehicle.modelYear}</span>
          </div>
          <h3 className="text-base font-bold text-slate-900 line-clamp-1 group-hover:text-blue-600 transition">
            {vehicle.name}
          </h3>
          
          <div className="flex items-center gap-1.5 text-xs text-slate-500 mt-2">
            <MapPin className="w-3.5 h-3.5 text-blue-500 shrink-0" />
            <span className="truncate">{vehicle.location}</span>
          </div>

          {/* Quick Specs Grid */}
          <div className="grid grid-cols-3 gap-2 mt-4 py-3 border-y border-slate-100 text-xs text-slate-600">
            <div className="flex items-center gap-1.5">
              <Fuel className="w-3.5 h-3.5 text-blue-500 shrink-0" />
              <span className="truncate">{vehicle.fuelType}</span>
            </div>
            <div className="flex items-center gap-1.5">
              <Gauge className="w-3.5 h-3.5 text-blue-500 shrink-0" />
              <span className="truncate">{vehicle.transmission}</span>
            </div>
            <div className="flex items-center gap-1.5">
              <Users className="w-3.5 h-3.5 text-blue-500 shrink-0" />
              <span>{vehicle.seatingCapacity} Seats</span>
            </div>
          </div>
        </div>

        {/* Action Button */}
        <div className="mt-5">
          <Link
            to={`/vehicles/${vehicle.id}`}
            className="w-full flex items-center justify-center gap-2 px-4 py-2.5 rounded-xl bg-slate-900 hover:bg-blue-600 text-white text-xs font-bold transition shadow-sm"
          >
            <span>View Details & Reserve</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </Link>
        </div>

      </div>

    </div>
  );
}
