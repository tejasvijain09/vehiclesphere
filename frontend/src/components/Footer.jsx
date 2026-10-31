import React from 'react';
import { Link } from 'react-router-dom';
import { Car, Phone, Mail, MapPin, User, Sparkles } from 'lucide-react';

export default function Footer() {
  return (
    <footer className="w-full bg-slate-900 text-slate-300 border-t border-slate-800 pt-16 pb-12 overflow-x-hidden">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-10 pb-12 border-b border-slate-800">
          
          {/* Brand Info */}
          <div className="space-y-4 md:col-span-1">
            <div className="flex items-center gap-2">
              <div className="w-9 h-9 rounded-xl bg-blue-600 flex items-center justify-center text-white shadow-md shadow-blue-500/20">
                <Car className="w-5 h-5" />
              </div>
              <span className="text-xl font-extrabold text-white tracking-tight">
                Vehicle<span className="text-blue-500">Sphere</span>
              </span>
            </div>
            <p className="text-sm text-slate-400 leading-relaxed">
              Premium wedding and event vehicle rentals for memorable celebrations across India.
            </p>
            <div className="flex flex-wrap items-center gap-2 text-xs text-slate-500 pt-1">
              <span className="px-2.5 py-1 bg-slate-800 rounded-md border border-slate-700 text-slate-300">Spring Boot 3</span>
              <span className="px-2.5 py-1 bg-slate-800 rounded-md border border-slate-700 text-slate-300">React 18</span>
              <span className="px-2.5 py-1 bg-slate-800 rounded-md border border-slate-700 text-slate-300">PostgreSQL</span>
            </div>
          </div>

          {/* Wedding Fleet Categories */}
          <div>
            <h4 className="text-sm font-bold text-white uppercase tracking-wider mb-4">Wedding Fleet</h4>
            <ul className="space-y-2.5 text-sm">
              <li>
                <Link to="/vehicles" className="hover:text-white transition">Browse All Vehicles</Link>
              </li>
              <li>
                <Link to="/vehicles?categoryId=4" className="hover:text-white transition">Royal Luxury & Groom Entries</Link>
              </li>
              <li>
                <Link to="/vehicles?categoryId=3" className="hover:text-white transition">Baraat SUVs & Convoys</Link>
              </li>
              <li>
                <Link to="/vehicles?categoryId=5" className="hover:text-white transition">Electric Bridal Cars</Link>
              </li>
              <li>
                <Link to="/vehicles?categoryId=2" className="hover:text-white transition">Classic Wedding Sedans</Link>
              </li>
            </ul>
          </div>

          {/* Customer Portal */}
          <div>
            <h4 className="text-sm font-bold text-white uppercase tracking-wider mb-4">Celebration Portal</h4>
            <ul className="space-y-2.5 text-sm">
              <li>
                <Link to="/dashboard" className="hover:text-white transition">Rental Dashboard</Link>
              </li>
              <li>
                <Link to="/my-bookings" className="hover:text-white transition">My Event Bookings</Link>
              </li>
              <li>
                <Link to="/profile" className="hover:text-white transition">Account Profile</Link>
              </li>
              <li>
                <a 
                  href="http://localhost:8080/swagger-ui/index.html" 
                  target="_blank" 
                  rel="noreferrer" 
                  className="hover:text-white transition text-blue-400 inline-flex items-center gap-1"
                >
                  <span>OpenAPI Swagger UI</span>
                  <span className="text-[10px] font-mono">↗</span>
                </a>
              </li>
            </ul>
          </div>

          {/* Academic & Contact Details */}
          <div>
            <h4 className="text-sm font-bold text-white uppercase tracking-wider mb-4">Project Information</h4>
            <div className="space-y-3 text-sm text-slate-400">
              <div className="flex items-start gap-2.5">
                <User className="w-4 h-4 text-blue-500 shrink-0 mt-0.5" />
                <span className="text-slate-300 font-medium">Tejasvi, B.Tech CSE, Lovely Professional University</span>
              </div>
              <div className="flex items-center gap-2.5">
                <Mail className="w-4 h-4 text-blue-500 shrink-0" />
                <a href="mailto:tejasvijain09@gmail.com" className="hover:text-white transition">tejasvijain09@gmail.com</a>
              </div>
              <div className="flex items-center gap-2.5">
                <Phone className="w-4 h-4 text-blue-500 shrink-0" />
                <a href="tel:+919815408330" className="hover:text-white transition">+919815408330</a>
              </div>
              <div className="flex items-center gap-2.5 text-xs text-slate-500 pt-1">
                <MapPin className="w-4 h-4 text-blue-500 shrink-0" />
                <span>Punjab & Delhi NCR, India</span>
              </div>
            </div>
          </div>

        </div>

        {/* Bottom bar */}
        <div className="pt-8 flex flex-col sm:flex-row items-center justify-between text-xs text-slate-500 gap-4 text-center sm:text-left">
          <p>© 2026 Tejasvi — DevOps Project</p>
          <div className="flex flex-wrap items-center justify-center sm:justify-end gap-x-6 gap-y-2">
            <span>Automated CI/CD & Observability Architecture</span>
            <span className="text-emerald-400 flex items-center gap-1.5 font-medium">
              <span className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
              PostgreSQL Connected
            </span>
          </div>
        </div>
      </div>
    </footer>
  );
}
