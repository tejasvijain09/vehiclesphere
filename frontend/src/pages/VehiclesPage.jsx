import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import VehicleCard from '../components/VehicleCard';
import { Search, RotateCcw, Car } from 'lucide-react';

export default function VehiclesPage() {
  const { user } = useAuth();
  const [searchParams, setSearchParams] = useSearchParams();
  const [vehicles, setVehicles] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [wishlistIds, setWishlistIds] = useState(new Set());

  // Filters
  const [search, setSearch] = useState(searchParams.get('search') || '');
  const [categoryId, setCategoryId] = useState(searchParams.get('categoryId') || '');
  const [fuelType, setFuelType] = useState(searchParams.get('fuelType') || '');
  const [maxPrice, setMaxPrice] = useState(searchParams.get('maxPrice') || '');
  const [availableOnly, setAvailableOnly] = useState(searchParams.get('availableOnly') === 'true');
  const [sortBy, setSortBy] = useState('priceAsc');

  // Load categories and handle ?category=Name as well as ?categoryId=ID
  useEffect(() => {
    api.get('/categories')
      .then((res) => {
        setCategories(res.data);
        const catNameParam = searchParams.get('category');
        const catIdParam = searchParams.get('categoryId');
        if (catIdParam) {
          setCategoryId(catIdParam);
        } else if (catNameParam) {
          const matched = res.data.find(
            (c) => c.name.toLowerCase() === catNameParam.toLowerCase()
          );
          if (matched) {
            setCategoryId(String(matched.id));
          }
        }
      })
      .catch((err) => console.error('Failed to load categories', err));
  }, [searchParams]);

  // Load wishlist IDs if user is logged in
  useEffect(() => {
    if (user) {
      api.get('/wishlist')
        .then((res) => setWishlistIds(new Set(res.data.map((v) => v.id))))
        .catch(() => {});
    } else {
      setWishlistIds(new Set());
    }
  }, [user]);

  // Sync state if URL query params update
  useEffect(() => {
    const qSearch = searchParams.get('search');
    if (qSearch !== null) setSearch(qSearch);
    const qCatId = searchParams.get('categoryId');
    if (qCatId !== null) setCategoryId(qCatId);
    const qFuel = searchParams.get('fuelType');
    if (qFuel !== null) setFuelType(qFuel);
  }, [searchParams]);

  // Fetch vehicles whenever filter changes
  useEffect(() => {
    const fetchVehicles = async () => {
      setLoading(true);
      try {
        const params = new URLSearchParams();
        if (search) params.append('search', search);
        if (categoryId) params.append('categoryId', categoryId);
        if (fuelType) params.append('fuelType', fuelType);
        if (maxPrice) params.append('maxPrice', maxPrice);
        if (availableOnly) params.append('availableOnly', 'true');

        const res = await api.get(`/vehicles?${params.toString()}`);
        let data = res.data;

        // Sorting
        if (sortBy === 'priceAsc') {
          data.sort((a, b) => Number(a.pricePerDay) - Number(b.pricePerDay));
        } else if (sortBy === 'priceDesc') {
          data.sort((a, b) => Number(b.pricePerDay) - Number(a.pricePerDay));
        } else if (sortBy === 'yearDesc') {
          data.sort((a, b) => b.modelYear - a.modelYear);
        }

        setVehicles(data);
      } catch (err) {
        console.error('Failed to load vehicles', err);
      } finally {
        setLoading(false);
      }
    };

    fetchVehicles();
  }, [search, categoryId, fuelType, maxPrice, availableOnly, sortBy]);

  const handleResetFilters = () => {
    setSearch('');
    setCategoryId('');
    setFuelType('');
    setMaxPrice('');
    setAvailableOnly(false);
    setSortBy('priceAsc');
    setSearchParams({});
  };

  return (
    <div className="w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8 overflow-x-hidden">
      
      {/* Header */}
      <div>
        <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">
          Wedding & Celebration Fleet
        </h1>
        <p className="text-xs sm:text-sm text-slate-500 mt-1">
          Explore luxury bridal cars, baraat SUVs, and executive sedans ready for your special occasions.
        </p>
      </div>

      {/* Filter and Search Bar */}
      <div className="bg-white rounded-2xl p-4 sm:p-6 border border-slate-200/80 shadow-sm space-y-5">
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          
          {/* Keyword Search */}
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1.5">
              Search by Model or Brand
            </label>
            <div className="relative">
              <Search className="w-4 h-4 text-slate-400 absolute left-3 top-3.5" />
              <input
                type="text"
                placeholder="e.g. Fortuner, BMW, Scorpio..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="w-full pl-9 pr-3 py-2.5 text-xs sm:text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 bg-slate-50"
              />
            </div>
          </div>

          {/* Category Filter */}
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1.5">
              Category
            </label>
            <select
              value={categoryId}
              onChange={(e) => setCategoryId(e.target.value)}
              className="w-full px-3 py-2.5 text-xs sm:text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 bg-slate-50"
            >
              <option value="">All Categories</option>
              {categories.map((c) => (
                <option key={c.id} value={c.id}>{c.name}</option>
              ))}
            </select>
          </div>

          {/* Fuel Type Filter */}
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1.5">
              Fuel Type
            </label>
            <select
              value={fuelType}
              onChange={(e) => setFuelType(e.target.value)}
              className="w-full px-3 py-2.5 text-xs sm:text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 bg-slate-50"
            >
              <option value="">Any Fuel Type</option>
              <option value="Electric">Electric</option>
              <option value="Petrol">Petrol</option>
              <option value="Diesel">Diesel</option>
              <option value="Hybrid">Hybrid</option>
            </select>
          </div>

          {/* Max Daily Rate (₹) */}
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-1.5">
              MAX DAILY RATE (₹)
            </label>
            <input
              type="number"
              placeholder="e.g. 50000"
              value={maxPrice}
              onChange={(e) => setMaxPrice(e.target.value)}
              className="w-full px-3 py-2.5 text-xs sm:text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 bg-slate-50"
            />
          </div>

        </div>

        {/* Sorting, Available Only & Reset */}
        <div className="flex flex-wrap items-center justify-between pt-4 border-t border-slate-100 gap-4">
          <div className="flex items-center gap-4 sm:gap-6">
            <label className="flex items-center gap-2 text-xs font-bold text-slate-700 cursor-pointer">
              <input
                type="checkbox"
                checked={availableOnly}
                onChange={(e) => setAvailableOnly(e.target.checked)}
                className="w-4 h-4 text-blue-600 rounded border-slate-300 focus:ring-blue-500"
              />
              <span>Available Only</span>
            </label>

            <button
              onClick={handleResetFilters}
              className="inline-flex items-center gap-1.5 text-xs font-semibold text-slate-500 hover:text-slate-800 transition"
            >
              <RotateCcw className="w-3.5 h-3.5" />
              <span>Reset Filters</span>
            </button>
          </div>

          <div className="flex items-center gap-2">
            <span className="text-xs font-semibold text-slate-500">Sort by:</span>
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              className="px-3 py-1.5 text-xs font-semibold rounded-lg border border-slate-200 bg-slate-50 focus:outline-none"
            >
              <option value="priceAsc">Price: Low to High (₹)</option>
              <option value="priceDesc">Price: High to Low (₹)</option>
              <option value="yearDesc">Newest Model Year</option>
            </select>
          </div>
        </div>
      </div>

      {/* Results Count */}
      <div className="flex items-center justify-between text-xs text-slate-500">
        <p>Showing <span className="font-bold text-slate-900">{vehicles.length}</span> wedding rentals</p>
      </div>

      {/* Grid */}
      {loading ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {[1, 2, 3, 4, 5, 6].map((i) => (
            <div key={i} className="h-80 bg-slate-200 animate-pulse rounded-2xl"></div>
          ))}
        </div>
      ) : vehicles.length === 0 ? (
        <div className="bg-white rounded-2xl p-12 text-center border border-slate-200 shadow-sm space-y-3">
          <Car className="w-12 h-12 text-slate-300 mx-auto" />
          <h3 className="text-base font-bold text-slate-800">No Vehicles Matched Your Search</h3>
          <p className="text-xs text-slate-500 max-w-sm mx-auto">
            Try adjusting your budget or category filters to explore available wedding cars.
          </p>
          <div className="pt-2">
            <button
              onClick={handleResetFilters}
              className="px-4 py-2 bg-blue-600 text-white text-xs font-bold rounded-xl shadow-sm hover:bg-blue-700 transition"
            >
              Reset Filters
            </button>
          </div>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {vehicles.map((v) => (
            <VehicleCard
              key={v.id}
              vehicle={v}
              initialWishlisted={wishlistIds.has(v.id)}
            />
          ))}
        </div>
      )}

    </div>
  );
}
