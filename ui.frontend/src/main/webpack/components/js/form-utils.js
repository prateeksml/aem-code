/**
 * Creates a generic key-value map with methods to put and get values with anything as key (even object references).
 * @template K, V
 * @returns {{
 *   put: (key: K, value: V) => void,
 *   get: (key: K) => V | undefined
 * }}
 */
function GenericKeyValueStore() {
  let keys = [],
    values = [];

  return {
    /**
     * Puts a key-value pair into the map. If the key already exists, it updates the value.
     * @param {K} key - The key to associate with the value.
     * @param {V} value - The value to store in the map.
     */
    put: function (key, value) {
      let index = keys.indexOf(key);
      if (index == -1) {
        keys.push(key);
        values.push(value);
      } else {
        values[index] = value;
      }
    },
    /**
     * Gets the value associated with the specified key.
     * @param {K} key - The key to look up.
     * @returns {V|undefined} - The value associated with the key, or undefined if the key is not found.
     */
    get: function (key) {
      return values[keys.indexOf(key)];
    },
  };
}

export const FormValueChangedListener = (function () {
  function WatchForChange(formElement) {
    // Initialize listeners array
    const listeners = [];
    const formValues = {};

    //store initial values
    for (let i = 0; i < formElement.elements.length; i++) {
      const field = formElement.elements[i];
      if (
        field.type !== "submit" &&
        field.type !== "button" &&
        field.type !== "reset"
      ) {
        formValues[field.name] = field.value;
      }
    }

    //Function to add a listener
    function addChangeListener(callback, elementNames = []) {
      listeners.push({ callback, elementNames });
    }

    //Helper function to deep compare objects
    function deepEqual(obj1, obj2) {
      return JSON.stringify(obj1) === JSON.stringify(obj2);
    }
    function triggerCallbacks(flagValue, currentFormValues) {
      listeners.forEach((listener) => {
        const elementsToCheck = listener.elementNames;
        if (elementsToCheck.length === 0) {
          // No specific elements specified, notify for all changes
          listener.callback(flagValue);
        } else {
          const changedElements = elementsToCheck.filter(
            (e = Object.prototype.hasOwnProperty.call(currentFormValues, e))
          );
          if (changedElements.length > 0) {
            listener.callback(flagValue);
          } else {
            listener.callback(false);
          }
        }
      });
    }
    //Function to check for changes and notify listeners
    function checkForChanges() {
      const currentFormValues = {};

      //Iterate through form elements and store their current values
      const elemets = formElement.elements;
      for (let i = 0; i < elemets.length; i++) {
        const element = elemets[i];
        if (
          element.type !== "submit" &&
          element.type !== "button" &&
          element.type !== "reset"
        ) {
          currentFormValues[element.name] = element.value;
        }
      }

      //Check if any form value has changed
      const hasValueChanged = !deepEqual(formValues, currentFormValues);

      //notify listeners
      triggerCallbacks(hasValueChanged, currentFormValues);
    }

    // Attach a change event listener to form elements
    const formElements = formElement.elements;
    for (let i = 0; i < formElements.length; i++) {
      const element = formElements[i];
      if (
        element.type !== "submit" &&
        element.type !== "button" &&
        element.type !== "reset" &&
        element.tagName != "INPUT"
      ) {
        element.addEventListener("change", checkForChanges);
      } else if (element.tagName == "INPUT") {
        element.addEventListener("input", scheduleCheckForChange());
      }
    }

    formElement.addEventListener("reset", () => {
      setTimeout(() => triggerCallbacks(false, {}));
    });

    function scheduleCheckForChange() {
      let H;
      return () => {
        if (H) {
          clearTimeout(H);
        }
        H = setTimeout(checkForChanges, 200);
      };
    }

    return {
      addChangeListener,
    };
  }

  let InstanceStore = GenericKeyValueStore();
  function getInstance(form) {
    let instance = InstanceStore.get(form);
    if (!instance) {
      instance = WatchForChange(form);
      InstanceStore.put(form, instance);
    }
    return instance;
  }
  return { Watch: getInstance, getInstance };
})();
