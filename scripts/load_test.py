#!/usr/bin/env python3
"""
=============================================================================
VehicleSphere — Synthetic Traffic & Load Generator
=============================================================================
Final-Year B.Tech Computer Science Capstone Project
Automated Python test utility to generate realistic API traffic,
simulate concurrent customer journeys, and populate Prometheus metrics
and Grafana dashboards for demonstration.
=============================================================================
"""

import sys
import time
import json
import random
import argparse
from concurrent.futures import ThreadPoolExecutor, as_completed
from urllib.request import Request, urlopen
from urllib.error import HTTPError, URLError

DEFAULT_BASE_URL = "http://localhost:8080"
DEFAULT_CONCURRENCY = 5
DEFAULT_DURATION_SEC = 30

DEMO_USERS = [
    ("gurpreet.singh@example.com", "Customer@123"),
    ("simran.kaur@example.com", "Customer@123"),
    ("harleen.kaur@example.com", "Customer@123"),
    ("admin@vehiclesphere.com", "Admin@123"),
]

SEARCH_KEYWORDS = ["Rolls", "Mercedes", "Defender", "Fortuner", "Scorpio", "BMW", "Audi", "Land Cruiser"]

class Stats:
    def __init__(self):
        self.total_requests = 0
        self.successful_requests = 0
        self.failed_requests = 0
        self.latencies = []

    def record(self, success: bool, latency: float):
        self.total_requests += 1
        if success:
            self.successful_requests += 1
        else:
            self.failed_requests += 1
        self.latencies.append(latency)

def make_request(method: str, url: str, headers: dict = None, body: dict = None) -> tuple:
    """Execute HTTP request using Python standard library."""
    req_headers = {"User-Agent": "VehicleSphere-LoadTest/1.0", "Accept": "application/json"}
    if headers:
        req_headers.update(headers)

    data = None
    if body is not None:
        data = json.dumps(body).encode("utf-8")
        req_headers["Content-Type"] = "application/json"

    req = Request(url, data=data, headers=req_headers, method=method)
    start_time = time.time()
    try:
        with urlopen(req, timeout=10) as response:
            latency = time.time() - start_time
            response_body = response.read().decode("utf-8")
            status = response.getcode()
            parsed = json.loads(response_body) if response_body else {}
            return True, status, latency, parsed
    except HTTPError as e:
        latency = time.time() - start_time
        return False, e.code, latency, {}
    except URLError as e:
        latency = time.time() - start_time
        return False, 0, latency, {}
    except Exception as e:
        latency = time.time() - start_time
        return False, 500, latency, {}

def authenticate(base_url: str, email: str, password: str) -> str:
    """Authenticate and obtain JWT token."""
    login_url = f"{base_url}/api/v1/auth/login"
    success, status, _, body = make_request("POST", login_url, body={"email": email, "password": password})
    if success and "token" in body:
        return body["token"]
    return None

def simulate_user_session(base_url: str, stats: Stats, stop_time: float):
    """Simulate a continuous customer browsing and booking flow."""
    email, password = random.choice(DEMO_USERS)
    token = authenticate(base_url, email, password)
    auth_headers = {"Authorization": f"Bearer {token}"} if token else {}

    while time.time() < stop_time:
        action = random.choice(["health", "catalog", "search", "details", "bookings", "actuator"])

        if action == "health":
            # 1. Health check
            success, status, lat, _ = make_request("GET", f"{base_url}/actuator/health")
            stats.record(success, lat)

        elif action == "catalog":
            # 2. Browse full vehicle catalog
            success, status, lat, _ = make_request("GET", f"{base_url}/api/v1/vehicles")
            stats.record(success, lat)

        elif action == "search":
            # 3. Vehicle search with keyword
            kw = random.choice(SEARCH_KEYWORDS)
            success, status, lat, _ = make_request("GET", f"{base_url}/api/v1/vehicles/search?keyword={kw}")
            stats.record(success, lat)

        elif action == "details":
            # 4. View single vehicle details (id 1-14)
            veh_id = random.randint(1, 14)
            success, status, lat, _ = make_request("GET", f"{base_url}/api/v1/vehicles/{veh_id}")
            stats.record(success, lat)

        elif action == "bookings":
            # 5. Check customer bookings (authenticated)
            if token:
                success, status, lat, _ = make_request("GET", f"{base_url}/api/v1/bookings/my-bookings", headers=auth_headers)
                stats.record(success, lat)
            else:
                success, status, lat, _ = make_request("GET", f"{base_url}/api/v1/vehicles")
                stats.record(success, lat)

        elif action == "actuator":
            # 6. Prometheus Actuator scrape simulation
            success, status, lat, _ = make_request("GET", f"{base_url}/actuator/prometheus")
            stats.record(success, lat)

        # Brief realistic human pause between clicks (100ms - 500ms)
        time.sleep(random.uniform(0.1, 0.5))

def print_summary(stats: Stats, duration: float):
    print("\n" + "=" * 60)
    print(" VEHICLESPHERE — LOAD & TRAFFIC GENERATION SUMMARY")
    print("=" * 60)
    print(f" Elapsed Time:         {duration:.2f} seconds")
    print(f" Total HTTP Requests:  {stats.total_requests}")
    print(f" Successful (2xx):     {stats.successful_requests}")
    print(f" Failed / Non-2xx:     {stats.failed_requests}")
    if stats.total_requests > 0:
        throughput = stats.total_requests / duration
        avg_latency = (sum(stats.latencies) / len(stats.latencies)) * 1000
        stats.latencies.sort()
        p95 = stats.latencies[int(len(stats.latencies) * 0.95)] * 1000
        p99 = stats.latencies[int(len(stats.latencies) * 0.99)] * 1000

        print(f" Request Throughput:   {throughput:.2f} req/sec")
        print(f" Average Latency:      {avg_latency:.2f} ms")
        print(f" p95 Latency:          {p95:.2f} ms")
        print(f" p99 Latency:          {p99:.2f} ms")
    print("=" * 60)
    print(" PromQL & Grafana metrics successfully populated!")
    print(" Check dashboard at http://localhost:3000\n")

def main():
    parser = argparse.ArgumentParser(description="VehicleSphere Traffic & Load Generator")
    parser.add_argument("--url", default=DEFAULT_BASE_URL, help=f"Base URL (default: {DEFAULT_BASE_URL})")
    parser.add_argument("--users", type=int, default=DEFAULT_CONCURRENCY, help=f"Virtual users (default: {DEFAULT_CONCURRENCY})")
    parser.add_argument("--duration", type=int, default=DEFAULT_DURATION_SEC, help=f"Duration in seconds (default: {DEFAULT_DURATION_SEC}s)")
    args = parser.parse_args()

    print("\n" + "=" * 60)
    print(f" VehicleSphere Load Testing Starting...")
    print(f" Target Base URL:      {args.url}")
    print(f" Concurrent Workers:   {args.users}")
    print(f" Test Duration:        {args.duration}s")
    print("=" * 60)

    # Initial connectivity probe
    print(" Probing backend health at /actuator/health...")
    alive, status, lat, body = make_request("GET", f"{args.url}/actuator/health")
    if not alive:
        print(f" [WARNING] Backend probe returned status {status}. Proceeding anyway...")
    else:
        print(f" [SUCCESS] Backend is healthy! (Probe took {lat*1000:.1f}ms)")

    stats = Stats()
    start_time = time.time()
    stop_time = start_time + args.duration

    with ThreadPoolExecutor(max_workers=args.users) as executor:
        futures = [executor.submit(simulate_user_session, args.url, stats, stop_time) for _ in range(args.users)]
        for future in as_completed(futures):
            try:
                future.result()
            except Exception as e:
                print(f" Worker exception: {e}")

    total_duration = time.time() - start_time
    print_summary(stats, total_duration)

if __name__ == "__main__":
    main()
