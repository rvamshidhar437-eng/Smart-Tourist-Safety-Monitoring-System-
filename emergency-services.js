// Emergency Services Page - Dynamic Service Loading
document.addEventListener('DOMContentLoaded', function() {
    loadEmergencyServices();
    initializeFilters();
    initializeSearch();
    initializeCategoryFilters();
});

// Load Emergency Services from API
function loadEmergencyServices() {
    const servicesContainer = document.getElementById('servicesContainer');
    
    // Mock data - Replace with actual API call
    const mockServices = [
        {
            id: 1,
            name: 'Apollo Hospitals',
            type: 'hospital',
            address: '123 Healthcare St, City Center',
            phone: '+91-987-654-3210',
            distance: '2.5 km',
            latitude: 40.7128,
            longitude: -74.0060
        },
        {
            id: 2,
            name: 'City Police Station',
            type: 'police',
            address: '456 Law Enforcement Ave',
            phone: '100',
            distance: '1.2 km',
            latitude: 40.7130,
            longitude: -74.0065
        },
        {
            id: 3,
            name: 'Fire Station #1',
            type: 'fire',
            address: '789 Emergency Rd',
            phone: '101',
            distance: '3.1 km',
            latitude: 40.7125,
            longitude: -74.0055
        },
        {
            id: 4,
            name: 'Tourist Information Center',
            type: 'tourist',
            address: '321 Tourism Blvd',
            phone: '+91-111-222-3333',
            distance: '0.8 km',
            latitude: 40.7135,
            longitude: -74.0070
        }
    ];

    displayServices(mockServices);
}

// Display Services
function displayServices(services) {
    const servicesContainer = document.getElementById('servicesContainer');
    
    if (services.length === 0) {
        servicesContainer.innerHTML = `
            <div class="col-12 text-center py-5">
                <i data-lucide="inbox" style="width: 48px; height: 48px; color: #999; opacity: 0.5;"></i>
                <p class="text-muted mt-3">No services found</p>
            </div>
        `;
        return;
    }

    const servicesHTML = services.map(service => `
        <div class="col-md-6 col-lg-4">
            <div class="feature-card service-card" data-type="${service.type}">
                <div class="d-flex justify-content-between align-items-start mb-3">
                    <h5 class="mb-0">${service.name}</h5>
                    <span class="badge bg-primary">${service.distance}</span>
                </div>
                <p class="text-muted-strong mb-2">
                    <i data-lucide="map-pin" class="me-2" style="width: 16px; height: 16px;"></i>${service.address}
                </p>
                <p class="text-muted-strong mb-3">
                    <i data-lucide="phone" class="me-2" style="width: 16px; height: 16px;"></i>
                    <a href="tel:${service.phone}" class="text-decoration-none">${service.phone}</a>
                </p>
                <div class="d-grid gap-2">
                    <button class="btn btn-primary btn-sm" onclick="getDirections(${service.latitude}, ${service.longitude})">
                        <i data-lucide="navigation" class="me-2" style="width: 14px; height: 14px;"></i>Get Directions
                    </button>
                    <a href="tel:${service.phone}" class="btn btn-success btn-sm">
                        <i data-lucide="phone" class="me-2" style="width: 14px; height: 14px;"></i>Call Now
                    </a>
                </div>
            </div>
        </div>
    `).join('');

    servicesContainer.innerHTML = servicesHTML;
    
    // Reinitialize Lucide icons
    if (typeof lucide !== 'undefined') {
        lucide.createIcons();
    }
}

// Initialize Filters
function initializeFilters() {
    const serviceFilter = document.getElementById('serviceFilter');
    
    if (serviceFilter) {
        serviceFilter.addEventListener('change', function() {
            filterServices(this.value);
        });
    }
}

// Filter Services
function filterServices(type) {
    const serviceCards = document.querySelectorAll('.service-card');
    
    serviceCards.forEach(card => {
        if (type === '' || card.getAttribute('data-type') === type) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
}

// Initialize Search
function initializeSearch() {
    const serviceSearch = document.getElementById('serviceSearch');
    
    if (serviceSearch) {
        serviceSearch.addEventListener('input', function() {
            searchServices(this.value);
        });
    }
}

// Search Services
function searchServices(query) {
    const serviceCards = document.querySelectorAll('.service-card');
    const lowerQuery = query.toLowerCase();
    
    serviceCards.forEach(card => {
        const text = card.textContent.toLowerCase();
        if (text.includes(lowerQuery)) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
}

// Initialize Category Filters
function initializeCategoryFilters() {
    const categoryCards = document.querySelectorAll('.category-card');
    const serviceFilter = document.getElementById('serviceFilter');
    
    categoryCards.forEach(card => {
        card.addEventListener('click', function() {
            const category = this.getAttribute('data-category');
            if (serviceFilter) {
                serviceFilter.value = category;
                serviceFilter.dispatchEvent(new Event('change'));
            }
            filterServices(category);
        });
    });
}

// Get Directions
function getDirections(latitude, longitude) {
    const googleMapsUrl = `https://www.google.com/maps/dir/?api=1&destination=${latitude},${longitude}`;
    window.open(googleMapsUrl, '_blank');
}
