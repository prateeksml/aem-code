import { registerComponent } from "./component";

function IndustryservicegridExpandCollapse() {
  const industryservicegrid = document.querySelectorAll(".industryservicegrid");
  industryservicegrid.forEach((elem) => {
    if (elem.classList.contains("dark-theme")) {
      elem
        .querySelector(".button-view-all")
        .parentElement.classList.add("dark-theme");
      elem
        .querySelector(".button-view-all")
        .parentElement.classList.remove("light-theme");
    }
  });
  const industryservicegridButton =
    document.querySelectorAll(".button-view-all");
  industryservicegridButton.forEach((elem) => {
    elem.addEventListener("click", function (e) {
      const listItem = e.target
        .closest('.cmp-industryservicegrid[data-show="true"]')
        .querySelectorAll(".cmp-col").length;
      this.classList.toggle("active");
      e.target
        .closest('.cmp-industryservicegrid[data-show="true"]')
        .classList.toggle("active");
      if (listItem > 4) {
        this.classList.contains("active")
          ? (this.querySelector(".cmp-button__text").innerHTML =
              this.getAttribute("data-less"))
          : (this.querySelector(".cmp-button__text").innerHTML =
              this.getAttribute("data-show"));
      }
    });
  });
}

registerComponent(IndustryservicegridExpandCollapse, {
  selectors: {
    self: ".industryservicegrid",
  },
});
