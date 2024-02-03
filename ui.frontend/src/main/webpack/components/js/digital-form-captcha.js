/**
 * @typedef {Object} ReCaptchaV2.Parameters
 * @property {string} [sitekey] - Your sitekey.
 * @property {("light"|"dark")} [theme="light"] - The color theme of the widget.
 * @property {("audio"|"image")} [type="image"] - The type of CAPTCHA to serve.
 * @property {("compact"|"normal"|"invisible")} [size="compact"] - The size of the widget.
 * @property {number} [tabindex] - The tabindex of the widget and challenge.
 * @property {("bottomright"|"bottomleft")
 * } [badge="bottomright"] - The badge location for g-recaptcha with size of "invisible".
 * @property {boolean} [isolated=false] - Invisible reCAPTCHA only.
 * @property {(response: string) => void} [callback] - Your callback function for a successful CAPTCHA response.
 * @property {() => void} ["expired-callback"] - Your callback function for an expired CAPTCHA response.
 * @property {() => void} ["error-callback"] - Your callback function for a reCAPTCHA error.
 * @typedef {Object} ReCaptchaV2.ReCaptcha
 * @property {(container: string | HTMLElement, parameters?:
 *  ReCaptchaV2.Parameters, inherit?: boolean) => number} render - Renders the reCAPTCHA widget.
 * @property {(opt_widget_id?: number) => void} reset - Resets the reCAPTCHA widget.
 * @property {(opt_widget_id?: number) => string} getResponse - Gets the response for the reCAPTCHA widget.
 * @property {(opt_widget_id?: number) => PromiseLike<void>} execute - Programatically invokes the reCAPTCHA check.
 * @property {(siteKey: string, action: ReCaptchaV2.Action) =>
 * PromiseLike<string>} execute - Programatically invokes the reCAPTCHA check.
 * @property {(callback: () => void) => void} ready - Runs a function when the reCAPTCHA library has loaded.
 * @typedef {Object} ReCaptchaV2.Action
 * @property {string} action - The name of the action.
 * @typedef {Object} ReCaptchaV2.ReCaptchaV2
 * @property {ReCaptchaV2.ReCaptcha & { enterprise: ReCaptchaV2.ReCaptcha; }} grecaptcha - Main reCAPTCHA object.
 * @namespace ReCaptchaV2
 */

/**
 * Capcha Settings
 * @typedef {Object} CaptchaConfig
 * @property {boolean} Enabled
 * @property {"V2"|"V3"} Version
 * @property {string} SiteKey
 * @property {string} Script
 * @property {("compact"|"normal"|"invisible")} [Size="compact"] - The size of the widget.
 */

const CAPTCHA_VERSION_V2 = "V2",
  CAPTCHA_VERSION_V3 = "V3";

export class FormCaptcha {
  static WidthIdCounter = 0;
  /**
   * @param {HTMLFormElement} form
   */
  constructor(form) {
    /**@type {CaptchaConfig}*/
    this.Config = this._getConfig(form);
    this.IsCaptchaEnabled = false;
    /**@type {ReCaptchaV2.ReCaptcha} */
    this.CaptchaAPI = null;
    /**@type {number} */
    this.WidgetId = null;
    this.ui = { form, widgetDiv: null };
    this.CaptchaToken = "";
    this.IsPeopleContactForm = form.dataset.formType == "peopleContact";

    this._attachEvents();
  }
  /**
   * @param {HTMLFormElement} form
   * @returns {CaptchaConfig}
   */
  _getConfig(form) {
    return Object.freeze({
      Enabled: form.dataset.captchaEnabled === "true",
      Version: form.dataset.captchaVersion,
      SiteKey: form.dataset.captchaSitekey,
      Script: form.dataset.captchaScript,
      Size: form.dataset.captchaSize || "invisible",
    });
  }
  /**
   * @param {string} action name of action to send to catpcha api
   * @param {(token:String)=>} cb the sync function to be called once the token is generated.
   *
   * Calling execute may trigger captcha challenge UI for V2
   */
  Execute(cb, action) {
    if (!this.IsCaptchaEnabled) {
      setTimeout(() => cb(""), 16);
      return;
    }

    if (this.Config.Version == CAPTCHA_VERSION_V2) {
      this.cb = cb;
      return this.CaptchaAPI.execute(this.WidgetId);
    }
    //v3
    let promiseLike = action
      ? this.CaptchaAPI.execute(this.Config.SiteKey, { action })
      : this.CaptchaAPI.execute(this.Config.SiteKey);
    promiseLike.then((token) => {
      this.CaptchaToken = token;
      let field = document.getElementById("g-recaptcha-response-v3-field");
      if (field) {
        field.value = token;
      }
      setTimeout(() => cb(token), 16);
    });
  }
  _attachEvents() {
    let scriptElement = this.ui.form.querySelector(
      ".google-captcha-trigger-script"
    );

    let isEnabledClassAdded = document.body.classList.contains(
      "google-captcha-enabled"
    );

    if (isEnabledClassAdded) {
      this.IsCaptchaEnabled = this.Config.Enabled;
      this.LoadCaptcha();
    }

    if (scriptElement) {
      //one trust enabled
      //when user allows google-recaptcha from onetrust manage cookies modal
      let handle,
        count = 0,
        Limit = 20;
      if (this.IsPeopleContactForm) {
        handle = setInterval(() => {
          if (count >= Limit) {
            clearInterval(handle);
            return;
          }
          count++;
          let element = this.ui.form.querySelector(
            ".google-captcha-trigger-script"
          );
          if (element && element.type == "text/javascript") {
            this.IsCaptchaEnabled = this.Config.Enabled;
            this.LoadCaptcha();
            clearInterval(handle);
          }
        }, 1000);
      } else {
        document.body.addEventListener(
          "onetrust-google-recaptcha-selected",
          () => {
            //only if its also enabled from authoring, then we load captcha
            this.IsCaptchaEnabled = this.Config.Enabled;
            this.LoadCaptcha();
          }
        );
      }
    } else {
      //one trust not enabled
      this.IsCaptchaEnabled = this.Config.Enabled;
      this.LoadCaptcha();
    }

    this.ui.form.addEventListener("form-submit-success", () => this._reset());
    this.ui.form.addEventListener("form-submit-error", () => this._reset());
  }
  _render(div) {
    this.WidgetId = this.CaptchaAPI.render(div, {
      sitekey: this.Config.SiteKey,
      size: this.Config.Size,
      callback: (e) => {
        this._callback(e);
      },
      "expired-callback": (e) => {
        this._captchaExpired(e);
      },
    });
  }
  _callback(token) {
    this.CaptchaToken = token;
    if (this.cb) {
      setTimeout(() => {
        this.cb(token);
        delete this.cb;
      }, 16);
    }
  }
  _captchaExpired() {
    this._reset();
  }
  _reset() {
    this.CaptchaToken = "";
    if (
      this.IsCaptchaEnabled &&
      this.CaptchaAPI &&
      this.Config.Version == CAPTCHA_VERSION_V2
    ) {
      this.CaptchaAPI.reset(this.WidgetId);
    }
  }
  _createScriptElement(cb) {
    let script = document.createElement("script");
    if (this.Config.Version == CAPTCHA_VERSION_V3) {
      let url = new URL(this.Config.Script);
      url.searchParams.set("render", this.Config.SiteKey);
      script.src = url.toString();
    } else {
      script.src = this.Config.Script;
    }
    script.onload = cb;
    script.defer = script.async = true;
    return script;
  }
  _onCaptchaScriptLoad() {
    this.CaptchaAPI = grecaptcha || window.grecaptcha;
    if (this.CaptchaAPI && this.CaptchaAPI.ready) {
      this.CaptchaAPI.ready(() => {
        this.InitCaptcha();
      });
    }
  }
  LoadCaptcha() {
    if (this.IsCaptchaEnabled && !this.CaptchaAPI) {
      let script = this._createScriptElement(() => this._onCaptchaScriptLoad());
      document.body.append(script);
    }
  }
  _createWidgetDiv() {
    let div = document.createElement("div");
    div.id = "captcha-widget-" + ++FormCaptcha.WidthIdCounter;
    this.ui.widgetDiv = div;
    this.ui.form.appendChild(div);
    return div;
  }
  InitCaptcha() {
    if (this.Config.Version == CAPTCHA_VERSION_V3) {
      return;
    }
    let div = this._createWidgetDiv();
    this._render(div);
  }
}
