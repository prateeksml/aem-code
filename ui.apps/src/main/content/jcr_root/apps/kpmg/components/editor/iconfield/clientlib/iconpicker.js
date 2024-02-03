(function (window, document, $) {
  class IconPickerElement extends HTMLElement {
    render() {
      this.iconVariant = this.getAttribute("icon-variant") || "filled";
      this.pickerSrc = this.getAttribute("picker-src") + "?iconVariant=" + this.iconVariant;
      this.value = this.value || this.getAttribute("value");
      this.name = this.getAttribute("name");
      this.classList.add("icon-picker");

      this.innerHTML = `
          <span class="icon-result-${this.iconVariant}" icon-picker-control-value=""></span>
          <button is="coral-button" class="coral-Form-field" icon-picker-control="" variant="quiet" type="button"></button>
          <button is="coral-button" class="coral-Form-field" icon-picker-clear="" variant="quiet" type="button">clear</button>
          <input icon-picker-control-value="" type="hidden" name="${this.name}"/>
      `;
      this.updateValue(this.value);
      this.initEvents();
    }
    initEvents() {

      this.clearButton.addEventListener("click", (e) => {
        this.updateValue("");
      });

      this.pickerButton.addEventListener("click", (e) => {
        e.preventDefault();
        var targetElement = e.target || e.srcElement;
        var control = $(targetElement);
        var state = this.getState(control);

        if (state.loading) {
          return;
        }

        if (state.el) {
          if (state.open) {
            state.api.cancel();
            this.cancel(control, state);
          } else {
            this.show(control, state);
          }
        } else {
          var src = this.pickerSrc;
          if (!src) {
            return;
          }
          state.loading = true;
          $.ajax({
            url: src,
            cache: false,
          })
            .then((html) =>
              $(window).adaptTo("foundation-util-htmlparser").parse(html)
            )
            .then((fragment) => $(fragment).children()[0])
            .then(
              (pickerEl) => {
                state.loading = false;
                state.el = pickerEl;
                state.api = $(pickerEl).adaptTo("foundation-picker");
                this.show(control, state);
              },
              function () {
                state.loading = false;
              }
            );
        }
      });
    }
    updateValue(value) {
      if (value) {
        this.clearButton.show();
      } else {
        this.clearButton.hide();
      }
      this.iconInput.value = value;
      this.iconPreview.innerHTML = value;
      this.pickerButton.innerHTML = value ? "Change Icon" : "Select Icon";
    }
    connectedCallback() {
      if (!this.rendered) {
        this.render();
        this.rendered = true;
      }
    }

    // static get observedAttributes() {
    //   return ["picker-src", "name", "value"];
    // }

    // attributeChangedCallback(name, oldValue, newValue) {
    //   this.render();
    // }
    get iconInput() {
      return this.querySelector("input[icon-picker-control-value]");
    }
    get iconPreview() {
      return this.querySelector("span[icon-picker-control-value]");
    }

    get pickerButton() {
      return this.querySelector("button[icon-picker-control]");
    }

    get clearButton() {
      return this.querySelector("button[icon-picker-clear");
    }

    handleSelections(control, state, selections) {
      if (selections && selections.length) {
        var selected = selections[0].value;
        this.updateValue(selected)
      }
    }

    show(control, state) {
      state.api.attach(this);

      state.api.pick(control[0], []).then(
        (selections) => {
          state.api.detach();
          state.open = false;

          this.handleSelections(control, state, selections);
        },
        () => {
          this.cancel(control, state);
        }
      );

      if ("focus" in state.api) {
        state.api.focus(last);
      } else {
        state.el.focus();
      }

      state.open = true;
    }

    cancel(control, state) {
      state.api.detach();
      state.open = false;
    }

    getState(control) {
      var KEY_STATE = "icon-picker-control.internal.state";

      var state = control.data(KEY_STATE);

      if (!state) {
        state = {
          el: null,
          open: false,
          loading: false,
        };
        control.data(KEY_STATE, state);
      }

      return state;
    }
  }
  window.customElements.define("icon-picker", IconPickerElement);
})(window, document, Granite.$);
