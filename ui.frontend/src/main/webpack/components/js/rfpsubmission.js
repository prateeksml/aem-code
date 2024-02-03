import { registerComponent } from "./component";
import { HandleEventFormSubmit, HandleRFPPrintEvnt } from "./datalayer";

// handles horizontal bleed for the registered components
function rfpSubmission(config) {
  if (
    localStorage.getItem("RFPRefNo") &&
    localStorage.getItem("RFPInformation")
  ) {
    const $element = config.element;
    const $contactInfoContainer = $element.querySelector(
      config.selectors.contactSection
    );
    const $requestInfoContainer = $element.querySelector(
      config.selectors.requestSection
    );
    const RFPRefNo = localStorage.getItem("RFPRefNo");
    try {
      HandleEventFormSubmit($element, "rfp", { rfpid: RFPRefNo });
    } catch (error) {
      console.warn("HandleEventFormSubmit", error);
    }
    const RFPInformation = JSON.parse(localStorage.getItem("RFPInformation"));
    let userName;
    $element.querySelector(config.selectors.refNoContainer).textContent =
      RFPRefNo;
    $contactInfoContainer.innerHTML = "";
    $requestInfoContainer.innerHTML = "";

    function splitAndTruncate(inputString) {
      // Split the inputString by ';'
      const parts = inputString.split(";");

      // Truncate text after '#' for each part
      const result = parts.map((part) => {
        const index = part.indexOf("#");
        return index !== -1 ? part.substring(0, index) : part;
      });

      return result;
    }
    RFPInformation.forEach((item) => {
      if (item.Value) {
        if (item.Key === "FirstName") {
          userName = item.Value;
        }
        if (item.Key === "LastName") {
          userName += " " + item.Value;
        }
        if (item.Key === "AlreadyClient") {
          item.Value = item.Value === "True" ? "Yes" : "No";
        }
        if (item.Key === "RFPPageURL") {
          return;
        }

        const divElement = document.createElement("div");
        divElement.classList.add("cmp-rfp-submission__info");

        const labelSpan = document.createElement("span");
        labelSpan.classList.add("label");
        labelSpan.textContent = item.Text
          ? item.Text + ":  "
          : item.Key + ":  ";
        divElement.appendChild(labelSpan);
        if (item.Key === "Attachments") {
          const valueHTML = document.createElement("ul");
          valueHTML.classList.add("cmp-rfp-submission__file-list");
          const filenames = splitAndTruncate(item.Value); // Replace with actual filenames
          filenames.forEach((filename) => {
            const liElement = document.createElement("li");
            liElement.textContent = filename;
            valueHTML.appendChild(liElement);
            divElement.appendChild(valueHTML);
          });
        } else {
          const valueHTML = document.createElement("span");
          valueHTML.textContent = item.Value;
          divElement.appendChild(valueHTML);
        }
        if (item.Key === "Message" || item.Key === "Attachments") {
          $requestInfoContainer.prepend(divElement);
        } else {
          $contactInfoContainer.appendChild(divElement);
        }
      }
    });
    $element.querySelector(config.selectors.nameContainer).textContent =
      userName;
  }
  const printBtn = config.element.querySelector(config.selectors.printButton);
  if (printBtn) {
    printBtn.addEventListener("click", HandleRFPPrintEvnt);
  }
}

// Register for Section Container
registerComponent(rfpSubmission, {
  selectors: {
    self: ".cmp-rfp-submission",
    contactSection: ".cmp-rfp-submission__contact-info",
    requestSection: ".cmp-rfp-submission__request-details",
    nameContainer: ".cmp-rfp-submission__name",
    refNoContainer: ".cmp-rfp-submission__ref-no",
    printButton: ".cmp-rfp-submission__button",
  },
});
