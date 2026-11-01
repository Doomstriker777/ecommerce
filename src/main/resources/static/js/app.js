/* ============================================================
   AB Mart — Main JavaScript
   ============================================================ */

document.addEventListener('DOMContentLoaded', function () {
  initNavbar();
  initMobileMenu();
  initSearchOverlay();
  initScrollAnimations();
  initBackToTop();
  initQuantitySelectors();
  initCartActions();
  initWishlistActions();
  initToastSystem();
  initFormValidation();
  initCategoryDropdown();
  initLazyLoading();
});

/* ============================================================
   NAVBAR SCROLL EFFECT
   ============================================================ */
function initNavbar() {
  const navbar = document.querySelector('.navbar');
  if (!navbar) return;

  function onScroll() {
    if (window.scrollY > 50) {
      navbar.classList.add('scrolled');
    } else {
      navbar.classList.remove('scrolled');
    }
  }

  window.addEventListener('scroll', onScroll, { passive: true });
  onScroll();
}

/* ============================================================
   MOBILE MENU TOGGLE
   ============================================================ */
function initMobileMenu() {
  const hamburger = document.querySelector('.hamburger');
  const navLinks = document.querySelector('.nav-links');

  if (!hamburger || !navLinks) return;

  hamburger.addEventListener('click', function () {
    hamburger.classList.toggle('active');
    navLinks.classList.toggle('active');
    document.body.style.overflow = navLinks.classList.contains('active') ? 'hidden' : '';
  });

  // Close menu on link click
  navLinks.querySelectorAll('a:not(.nav-dropdown-toggle)').forEach(function (link) {
    link.addEventListener('click', function () {
      hamburger.classList.remove('active');
      navLinks.classList.remove('active');
      document.body.style.overflow = '';
    });
  });

  // Close menu on outside click
  document.addEventListener('click', function (e) {
    if (!navLinks.contains(e.target) && !hamburger.contains(e.target) && navLinks.classList.contains('active')) {
      hamburger.classList.remove('active');
      navLinks.classList.remove('active');
      document.body.style.overflow = '';
    }
  });
}

/* ============================================================
   CATEGORY DROPDOWN (mobile)
   ============================================================ */
function initCategoryDropdown() {
  const dropdownToggles = document.querySelectorAll('.nav-dropdown-toggle');

  dropdownToggles.forEach(function (toggle) {
    toggle.addEventListener('click', function (e) {
      if (window.innerWidth <= 768) {
        e.preventDefault();
        const parent = toggle.closest('.nav-dropdown');
        parent.classList.toggle('open');
      }
    });
  });
}

/* ============================================================
   SEARCH OVERLAY
   ============================================================ */
function initSearchOverlay() {
  const searchIcon = document.querySelector('.search-toggle');
  const searchOverlay = document.querySelector('.search-overlay');
  const searchClose = document.querySelector('.search-close');
  const searchInput = document.querySelector('.search-overlay input');

  if (!searchIcon || !searchOverlay) return;

  searchIcon.addEventListener('click', function (e) {
    e.preventDefault();
    searchOverlay.classList.add('active');
    if (searchInput) {
      setTimeout(function () { searchInput.focus(); }, 200);
    }
  });

  if (searchClose) {
    searchClose.addEventListener('click', function () {
      searchOverlay.classList.remove('active');
    });
  }

  // Close on Escape
  document.addEventListener('keydown', function (e) {
    if (e.key === 'Escape' && searchOverlay.classList.contains('active')) {
      searchOverlay.classList.remove('active');
    }
  });

  // Handle search form submission
  if (searchInput) {
    searchInput.addEventListener('keydown', function (e) {
      if (e.key === 'Enter') {
        var q = searchInput.value.trim();
        if (q) {
          window.location.href = '/shop/search?q=' + encodeURIComponent(q);
        }
      }
    });
  }

  // Shop page search form
  var shopSearchForm = document.querySelector('.shop-search');
  if (shopSearchForm) {
    shopSearchForm.addEventListener('submit', function (e) {
      e.preventDefault();
      var input = shopSearchForm.querySelector('input');
      if (input && input.value.trim()) {
        window.location.href = '/shop/search?q=' + encodeURIComponent(input.value.trim());
      }
    });
  }
}

/* ============================================================
   TOAST NOTIFICATION SYSTEM
   ============================================================ */
var toastContainer = null;

function initToastSystem() {
  toastContainer = document.querySelector('.toast-container');
  if (!toastContainer) {
    toastContainer = document.createElement('div');
    toastContainer.className = 'toast-container';
    document.body.appendChild(toastContainer);
  }
}

function showToast(message, type) {
  type = type || 'info';
  if (!toastContainer) initToastSystem();

  var icons = {
    success: 'fas fa-check-circle',
    error: 'fas fa-exclamation-circle',
    info: 'fas fa-info-circle'
  };

  var toast = document.createElement('div');
  toast.className = 'toast toast-' + type;
  toast.innerHTML =
    '<i class="' + (icons[type] || icons.info) + '"></i>' +
    '<span>' + message + '</span>' +
    '<span class="toast-close">&times;</span>';

  toastContainer.appendChild(toast);

  // Close on click
  toast.querySelector('.toast-close').addEventListener('click', function () {
    removeToast(toast);
  });

  // Auto-remove after 4 seconds
  setTimeout(function () {
    removeToast(toast);
  }, 4000);
}

function removeToast(toast) {
  if (!toast || !toast.parentNode) return;
  toast.classList.add('hiding');
  setTimeout(function () {
    if (toast.parentNode) toast.parentNode.removeChild(toast);
  }, 300);
}

/* ============================================================
   CART ACTIONS (AJAX)
   ============================================================ */
function initCartActions() {
  // Add to cart buttons
  document.addEventListener('click', function (e) {
    var addBtn = e.target.closest('.btn-add-cart');
    if (addBtn) {
      e.preventDefault();
      var productId = addBtn.getAttribute('data-product-id');
      if (productId) {
        addToCart(productId, addBtn);
      }
    }
  });

  // Add to cart form on product detail page
  var addToCartForm = document.querySelector('.add-to-cart-form');
  if (addToCartForm) {
    addToCartForm.addEventListener('submit', function (e) {
      e.preventDefault();
      var productId = addToCartForm.getAttribute('data-product-id');
      var qtyInput = addToCartForm.querySelector('.qty-input');
      var quantity = qtyInput ? qtyInput.value : 1;
      addToCart(productId, null, quantity);
    });
  }

  // Update quantity in cart
  document.querySelectorAll('.cart-qty-form').forEach(function (form) {
    form.addEventListener('submit', function (e) {
      e.preventDefault();
    });
  });

  // Remove from cart
  document.addEventListener('click', function (e) {
    var removeBtn = e.target.closest('.cart-item-remove');
    if (removeBtn) {
      e.preventDefault();
      var itemId = removeBtn.getAttribute('data-item-id');
      if (itemId) {
        removeFromCart(itemId, removeBtn);
      }
    }
  });
}

function addToCart(productId, btn, quantity) {
  quantity = quantity || 1;

  var formData = new FormData();
  formData.append('quantity', quantity);

  // Get CSRF token
  var csrfToken = document.querySelector('meta[name="_csrf"]');
  var csrfHeader = document.querySelector('meta[name="_csrf_header"]');

  var headers = {};
  if (csrfToken && csrfHeader) {
    headers[csrfHeader.getAttribute('content')] = csrfToken.getAttribute('content');
  }

  fetch('/cart/add/' + productId, {
    method: 'POST',
    body: formData,
    headers: headers
  })
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else if (response.status === 401 || response.status === 403) {
        window.location.href = '/login';
        throw new Error('Unauthorized');
      }
      throw new Error('Failed');
    })
    .then(function () {
      showToast('Product added to cart!', 'success');
      updateCartCount(1);
      if (btn) {
        btn.innerHTML = '<i class="fas fa-check"></i> Added';
        setTimeout(function () {
          btn.innerHTML = '<i class="fas fa-shopping-cart"></i> Add to Cart';
        }, 2000);
      }
    })
    .catch(function (err) {
      if (err.message !== 'Unauthorized') {
        showToast('Failed to add product to cart.', 'error');
      }
    });
}

function updateCartQuantity(itemId, quantity) {
  var csrfToken = document.querySelector('meta[name="_csrf"]');
  var csrfHeader = document.querySelector('meta[name="_csrf_header"]');

  var headers = { 'Content-Type': 'application/x-www-form-urlencoded' };
  if (csrfToken && csrfHeader) {
    headers[csrfHeader.getAttribute('content')] = csrfToken.getAttribute('content');
  }

  fetch('/cart/update/' + itemId, {
    method: 'POST',
    body: 'quantity=' + quantity,
    headers: headers
  })
    .then(function (response) {
      if (response.ok) {
        window.location.reload();
      } else {
        showToast('Failed to update quantity.', 'error');
      }
    })
    .catch(function () {
      showToast('An error occurred.', 'error');
    });
}

function removeFromCart(itemId, btn) {
  var csrfToken = document.querySelector('meta[name="_csrf"]');
  var csrfHeader = document.querySelector('meta[name="_csrf_header"]');

  var headers = {};
  if (csrfToken && csrfHeader) {
    headers[csrfHeader.getAttribute('content')] = csrfToken.getAttribute('content');
  }

  fetch('/cart/remove/' + itemId, {
    method: 'POST',
    headers: headers
  })
    .then(function (response) {
      if (response.ok) {
        showToast('Item removed from cart.', 'success');
        var row = btn.closest('tr');
        if (row) {
          row.style.opacity = '0';
          row.style.transform = 'translateX(-20px)';
          row.style.transition = 'all .3s ease';
          setTimeout(function () {
            row.remove();
            // Check if cart is empty
            var remaining = document.querySelectorAll('.cart-table tbody tr');
            if (remaining.length === 0) {
              window.location.reload();
            }
          }, 300);
        }
        updateCartCount(-1);
      } else {
        showToast('Failed to remove item.', 'error');
      }
    })
    .catch(function () {
      showToast('An error occurred.', 'error');
    });
}

function updateCartCount(delta) {
  var badge = document.querySelector('.cart-count');
  if (badge) {
    var current = parseInt(badge.textContent) || 0;
    var newCount = Math.max(0, current + delta);
    badge.textContent = newCount;
    if (newCount === 0) {
      badge.style.display = 'none';
    } else {
      badge.style.display = 'flex';
    }
    // Pulse animation
    badge.style.transform = 'scale(1.3)';
    setTimeout(function () { badge.style.transform = 'scale(1)'; }, 200);
  }
}

/* ============================================================
   WISHLIST TOGGLE (AJAX)
   ============================================================ */
function initWishlistActions() {
  document.addEventListener('click', function (e) {
    var wishBtn = e.target.closest('.wishlist-toggle');
    if (wishBtn) {
      e.preventDefault();
      var productId = wishBtn.getAttribute('data-product-id');
      if (productId) {
        toggleWishlist(productId, wishBtn);
      }
    }
  });
}

function toggleWishlist(productId, btn) {
  var csrfToken = document.querySelector('meta[name="_csrf"]');
  var csrfHeader = document.querySelector('meta[name="_csrf_header"]');

  var headers = {};
  if (csrfToken && csrfHeader) {
    headers[csrfHeader.getAttribute('content')] = csrfToken.getAttribute('content');
  }

  fetch('/wishlist/toggle/' + productId, {
    method: 'POST',
    headers: headers
  })
    .then(function (response) {
      if (response.ok) {
        return response.text();
      } else if (response.status === 401 || response.status === 403) {
        window.location.href = '/login';
        throw new Error('Unauthorized');
      }
      throw new Error('Failed');
    })
    .then(function () {
      btn.classList.toggle('active');
      var icon = btn.querySelector('i');
      if (icon) {
        if (btn.classList.contains('active')) {
          icon.classList.remove('far');
          icon.classList.add('fas');
          showToast('Added to wishlist!', 'success');
        } else {
          icon.classList.remove('fas');
          icon.classList.add('far');
          showToast('Removed from wishlist.', 'info');
        }
      }
      // Update wishlist count
      var wishCount = document.querySelector('.wishlist-count');
      if (wishCount) {
        var cur = parseInt(wishCount.textContent) || 0;
        var newVal = btn.classList.contains('active') ? cur + 1 : Math.max(0, cur - 1);
        wishCount.textContent = newVal;
        if (newVal === 0) {
          wishCount.style.display = 'none';
        } else {
          wishCount.style.display = 'flex';
        }
      }
    })
    .catch(function (err) {
      if (err.message !== 'Unauthorized') {
        showToast('Failed to update wishlist.', 'error');
      }
    });
}

/* ============================================================
   QUANTITY SELECTORS (+/- buttons)
   ============================================================ */
function initQuantitySelectors() {
  document.addEventListener('click', function (e) {
    var btn = e.target.closest('.qty-btn');
    if (!btn) return;

    var container = btn.closest('.quantity-selector') || btn.closest('.cart-item-quantity');
    if (!container) return;

    var input = container.querySelector('.qty-input');
    if (!input) return;

    var current = parseInt(input.value) || 1;
    var min = parseInt(input.getAttribute('min')) || 1;
    var max = parseInt(input.getAttribute('max')) || 999;

    if (btn.classList.contains('qty-minus') || btn.getAttribute('data-action') === 'minus') {
      if (current > min) input.value = current - 1;
    } else if (btn.classList.contains('qty-plus') || btn.getAttribute('data-action') === 'plus') {
      if (current < max) input.value = current + 1;
    }

    // If in cart, trigger update
    var itemId = container.getAttribute('data-item-id');
    if (itemId) {
      updateCartQuantity(itemId, input.value);
    }
  });
}

/* ============================================================
   SCROLL ANIMATIONS (Intersection Observer)
   ============================================================ */
function initScrollAnimations() {
  var animatedElements = document.querySelectorAll('.fade-in, .fade-in-left, .fade-in-right, .scale-in');

  if (animatedElements.length === 0) return;

  if ('IntersectionObserver' in window) {
    var observer = new IntersectionObserver(function (entries) {
      entries.forEach(function (entry) {
        if (entry.isIntersecting) {
          entry.target.classList.add('visible');
          observer.unobserve(entry.target);
        }
      });
    }, { threshold: 0.1, rootMargin: '0px 0px -50px 0px' });

    animatedElements.forEach(function (el) {
      observer.observe(el);
    });
  } else {
    // Fallback: show all
    animatedElements.forEach(function (el) {
      el.classList.add('visible');
    });
  }
}

/* ============================================================
   BACK TO TOP BUTTON
   ============================================================ */
function initBackToTop() {
  var btn = document.querySelector('.back-to-top');
  if (!btn) return;

  window.addEventListener('scroll', function () {
    if (window.scrollY > 400) {
      btn.classList.add('visible');
    } else {
      btn.classList.remove('visible');
    }
  }, { passive: true });

  btn.addEventListener('click', function () {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  });
}

/* ============================================================
   IMAGE LAZY LOADING
   ============================================================ */
function initLazyLoading() {
  var lazyImages = document.querySelectorAll('img[data-src]');

  if (lazyImages.length === 0) return;

  if ('IntersectionObserver' in window) {
    var imgObserver = new IntersectionObserver(function (entries) {
      entries.forEach(function (entry) {
        if (entry.isIntersecting) {
          var img = entry.target;
          img.src = img.getAttribute('data-src');
          img.removeAttribute('data-src');
          imgObserver.unobserve(img);
        }
      });
    }, { rootMargin: '50px' });

    lazyImages.forEach(function (img) {
      imgObserver.observe(img);
    });
  } else {
    lazyImages.forEach(function (img) {
      img.src = img.getAttribute('data-src');
      img.removeAttribute('data-src');
    });
  }
}

/* ============================================================
   FORM VALIDATION
   ============================================================ */
function initFormValidation() {
  // Register form
  var registerForm = document.querySelector('.register-form');
  if (registerForm) {
    registerForm.addEventListener('submit', function (e) {
      var password = registerForm.querySelector('#password');
      var confirmPassword = registerForm.querySelector('#confirmPassword');
      var errorDiv = registerForm.querySelector('.form-error-global');

      if (password && confirmPassword && password.value !== confirmPassword.value) {
        e.preventDefault();
        if (errorDiv) {
          errorDiv.textContent = 'Passwords do not match!';
          errorDiv.style.display = 'block';
        } else {
          showToast('Passwords do not match!', 'error');
        }
        confirmPassword.focus();
        return false;
      }

      // Basic validation
      var inputs = registerForm.querySelectorAll('[required]');
      for (var i = 0; i < inputs.length; i++) {
        if (!inputs[i].value.trim()) {
          e.preventDefault();
          inputs[i].focus();
          showToast('Please fill in all required fields.', 'error');
          return false;
        }
      }
    });
  }

  // Login form
  var loginForm = document.querySelector('.login-form');
  if (loginForm) {
    loginForm.addEventListener('submit', function (e) {
      var inputs = loginForm.querySelectorAll('[required]');
      for (var i = 0; i < inputs.length; i++) {
        if (!inputs[i].value.trim()) {
          e.preventDefault();
          inputs[i].focus();
          showToast('Please fill in all required fields.', 'error');
          return false;
        }
      }
    });
  }

  // Checkout form
  var checkoutForm = document.querySelector('.checkout-form');
  if (checkoutForm) {
    checkoutForm.addEventListener('submit', function (e) {
      var inputs = checkoutForm.querySelectorAll('[required]');
      for (var i = 0; i < inputs.length; i++) {
        if (!inputs[i].value.trim()) {
          e.preventDefault();
          inputs[i].focus();
          showToast('Please fill in all required fields.', 'error');
          return false;
        }
      }
    });
  }

  // Add visual feedback for form inputs
  document.querySelectorAll('.form-control').forEach(function (input) {
    input.addEventListener('blur', function () {
      if (input.hasAttribute('required') && !input.value.trim()) {
        input.style.borderColor = '#e74c3c';
      } else {
        input.style.borderColor = '';
      }
    });

    input.addEventListener('focus', function () {
      input.style.borderColor = '';
    });
  });
}

/* ============================================================
   STAR RATING INPUT (review form)
   ============================================================ */
document.addEventListener('click', function (e) {
  if (e.target.closest('.star-rating-input')) {
    var star = e.target.closest('i');
    if (!star) return;

    var container = star.closest('.star-rating-input');
    var stars = container.querySelectorAll('i');
    var value = parseInt(star.getAttribute('data-value'));
    var hiddenInput = container.parentElement.querySelector('input[name="rating"]');

    stars.forEach(function (s, idx) {
      if (idx < value) {
        s.classList.remove('far');
        s.classList.add('fas', 'active');
      } else {
        s.classList.remove('fas', 'active');
        s.classList.add('far');
      }
    });

    if (hiddenInput) hiddenInput.value = value;
  }
});

/* ============================================================
   IMAGE ERROR FALLBACK
   ============================================================ */
document.addEventListener('error', function (e) {
  if (e.target.tagName === 'IMG') {
    e.target.src = 'data:image/svg+xml,' + encodeURIComponent(
      '<svg xmlns="http://www.w3.org/2000/svg" width="300" height="300" viewBox="0 0 300 300">' +
      '<rect fill="#16213e" width="300" height="300"/>' +
      '<text fill="#555" font-family="sans-serif" font-size="14" x="50%" y="50%" text-anchor="middle" dy=".3em">Image not available</text>' +
      '</svg>'
    );
    e.target.style.objectFit = 'contain';
  }
}, true);

/* ============================================================
   SMOOTH SCROLL FOR ANCHOR LINKS
   ============================================================ */
document.addEventListener('click', function (e) {
  var link = e.target.closest('a[href^="#"]');
  if (link) {
    var targetId = link.getAttribute('href');
    if (targetId && targetId !== '#') {
      var target = document.querySelector(targetId);
      if (target) {
        e.preventDefault();
        target.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }
    }
  }
});
