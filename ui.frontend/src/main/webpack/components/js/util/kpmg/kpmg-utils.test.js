import * as kpmgUtils from "./kpmg-utils";

//Set up fixture for testing any methods that require a DOM
//TODO: Move this into a place where it can be reused by other tests
const fixture = `
  <div id="cmp-example" class="cmp-example" data-cmp-example="">
    <div id="cmp-example-target-by-id">Target By ID</div>
    <div class="cmp-example-target-by-class">Target By Class</div>
    <div data-cmp-example-target-by-data-attr="">Target By Data Attribute</div>
  </div>`;

describe("KPMG Utils: Test", () => {
  beforeEach(() => {
    document.body.innerHTML = fixture;
  });

  const getDocBody = () => document.body;

  test("Make sure fixture is added to document.body.innerHTML for DOM testing", () => {
    expect(document.body.innerHTML).toBe(fixture);
  });

  test("KPMGUtils are defined", () => {
    //console.log("kpmgUtils: ", kpmgUtils);
    expect(typeof kpmgUtils).toBe("object");
  });

  //Breakpoint Tests
  const breakpoints = kpmgUtils.breakpoint;

  test("Breakpoint object exists", () => {
    expect(typeof breakpoints).toBe("object");
  });

  test("Breakpoint object has required breakpoints and the returned values are expected", () => {
    expect(breakpoints["tablet"]).toBe(768);
    expect(breakpoints["desktop-small"]).toBe(1024);
    expect(breakpoints["desktop-medium"]).toBe(1440);
  });

  //Query
  const query = kpmgUtils.query;

  test("Query function exists", () => {
    expect(typeof query).toBe("function");
  });

  test("If selector string is not passed to query, throw error", () => {
    // eslint-disable-next-line max-len
    expect(() => query()).toThrow(
      // eslint-disable-next-line max-len
      `######## KPMGUtils: query: param of "selector" was an empty string. Please provide a valid selector! Possible types: id, class, data attribute.`
    );
  });

  test(`If "selector" param passed does not have a type of string, throw error`, () => {
    expect(() => query([])).toThrow(
      `######## KPMGUtils: query: param of "selector" needs be a type of string!`
    );
  });

  test(`If "options" param passed is not a type of object, throw error`, () => {
    // eslint-disable-next-line max-len
    expect(() => query("#cmp-example", "bad options")).toThrow(
      `######## KPMGUtils: query: param of "options" needs be a type of string!`
    );
  });

  //Retrieve parent element in fixture by id
  test("Query returns result when selector is id", async () => {
    //Retrieve parent element in fixture by id
    const foundSelectorById = await query("#cmp-example");
    expect(typeof foundSelectorById).toBe("object");
    expect(
      foundSelectorById.querySelector("#cmp-example-target-by-id").innerHTML
    ).toBe("Target By ID");
  });

  //Retrieve parent element in fixture by class
  test("Query returns result when selector is class", async () => {
    //Retrieve parent element in fixture by id
    const foundSelectorByClass = await query(".cmp-example");
    expect(typeof foundSelectorByClass).toBe("object");
    expect(
      foundSelectorByClass.querySelector("#cmp-example-target-by-id").innerHTML
    ).toBe("Target By ID");
  });

  //Retrieve parent element in fixture by data attr
  test("Query returns result when selector is data attr", async () => {
    //Retrieve parent element in fixture by id
    const foundSelectorByDataAttr = await query("[data-cmp-example]");
    expect(typeof foundSelectorByDataAttr).toBe("object");
    expect(
      foundSelectorByDataAttr.querySelector("#cmp-example-target-by-id")
        .innerHTML
    ).toBe("Target By ID");
  });

  test("Query returns false if no results were found in the DOM", async () => {
    const missingElement = await query("#foobar");
    expect(missingElement).toBeFalsy();
  });

  // test(`If "options.wait" is set to true, set up observer to watch for this selector, resolve once added`, async () => {

  //   //Add missing element after 1 second
  //   setTimeout(() => {
  //     const parentElement = document.querySelector("#cmp-example");
  //     const newElement = document.createElement("div");
  //     newElement.id = "#cmp-example__dynamically-added";
  //     newElement.textContent = "Dynamically Added Element";
  //     parentElement.appendChild(newElement);
  //   }, 1000);

  //   const elementToWaitFor = await query("#cmp-example__dynamically-added", { wait: true });

  //   console.log("HELLLO: ", elementToWaitFor);

  //   expect(elementToWaitFor);

  //   expect(1).toBe(1);

  // });
});
