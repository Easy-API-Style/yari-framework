document.querySelector('.theme-toggle-button').addEventListener('click', function () {
  var current = document.documentElement.getAttribute('data-theme') || 'dark';
  var next = current === 'dark' ? 'light' : 'dark';
  document.documentElement.setAttribute('data-theme', next);
  try { localStorage.setItem('yari-theme', next); } catch (e) {}
});

/* ---- copy-to-clipboard button on every code block ---- */
(function () {
  function fallbackCopy(text, done) {
    var ta = document.createElement('textarea');
    ta.value = text;
    ta.style.position = 'fixed';
    ta.style.top = '-9999px';
    ta.setAttribute('readonly', '');
    document.body.appendChild(ta);
    ta.select();
    try { document.execCommand('copy'); done(); } catch (e) {}
    document.body.removeChild(ta);
  }

  function addCopyButtons() {
    document.querySelectorAll('pre').forEach(function (pre) {
      if (pre.parentElement && pre.parentElement.classList.contains('code-wrap')) {
        return; // already processed
      }
      var code = pre.querySelector('code') || pre;

      var wrap = document.createElement('div');
      wrap.className = 'code-wrap';
      pre.parentNode.insertBefore(wrap, pre);
      wrap.appendChild(pre);

      var btn = document.createElement('button');
      btn.type = 'button';
      btn.className = 'copy-btn';
      btn.setAttribute('aria-label', 'Copy code');
      btn.textContent = 'Copy';

      btn.addEventListener('click', function () {
        var text = code.textContent;
        var done = function () {
          btn.textContent = 'Copied!';
          btn.classList.add('is-copied');
          setTimeout(function () {
            btn.textContent = 'Copy';
            btn.classList.remove('is-copied');
          }, 1600);
        };
        if (navigator.clipboard && navigator.clipboard.writeText) {
          navigator.clipboard.writeText(text).then(done, function () { fallbackCopy(text, done); });
        } else {
          fallbackCopy(text, done);
        }
      });

      wrap.appendChild(btn);
    });
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', addCopyButtons);
  } else {
    addCopyButtons();
  }
})();

/* ---- nav email button: copy address + show "Email copied!" in the tooltip ---- */
(function () {
  var btn = document.querySelector('.nav-email-button');
  if (!btn) return;

  function fallbackCopy(text, done) {
    var ta = document.createElement('textarea');
    ta.value = text;
    ta.style.position = 'fixed';
    ta.style.top = '-9999px';
    ta.setAttribute('readonly', '');
    document.body.appendChild(ta);
    ta.select();
    try { document.execCommand('copy'); done(); } catch (e) {}
    document.body.removeChild(ta);
  }

  var resetTimer;
  function showCopied() {
    // Swap the tooltip text → the "Copy email" tooltip is replaced by this one,
    // and it names the exact address that was copied.
    btn.setAttribute('data-tooltip', 'Copied ' + btn.dataset.email);
    btn.classList.add('is-copied');
    clearTimeout(resetTimer);
    resetTimer = setTimeout(function () {
      btn.classList.remove('is-copied');
      btn.setAttribute('data-tooltip', 'Copy email');
    }, 1600);
  }

  btn.addEventListener('click', function () {
    var email = btn.dataset.email;
    if (navigator.clipboard && navigator.clipboard.writeText) {
      navigator.clipboard.writeText(email).then(showCopied, function () { fallbackCopy(email, showCopied); });
    } else {
      fallbackCopy(email, showCopied);
    }
  });
})();
