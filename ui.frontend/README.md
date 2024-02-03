# Frontend Build

## Features

- Full TypeScript, ES6 and ES5 support (with applicable Webpack wrappers).
- TypeScript and JavaScript linting (using a TSLint ruleset – driven by ESLint - rules can be adjusted to suit your team's needs).
- ES5 output, for legacy browser support.
- Globbing
  - No need to add imports anywhere.
  - All JS and CSS files can now be added to each component (best practice is under /clientlib/js or /clientlib/(s)css)
  - No .content.xml or js.txt/css.txt files needed as everything is run through Webpack
  - The globber pulls in all JS files under the /component/ folder. Webpack allows CSS/SCSS files to be chained in via JS files. They are pulled in through sites.js.
  - The only files consumed by AEM are the output files site.js and site.css, the resources folder in /clientlib-site as well as dependencies.js and dependencies.css in /clientlib-dependencies
- Chunks
  - Main (site js/css)
- Full Sass/Scss support (Sass is compiled to CSS via Webpack).
- Static webpack development server with built in proxy to a local instance of AEM
- Automatic formatting with https://prettier.io

## Installation

1. Install [NodeJS](https://nodejs.org/en/download/) (v10+), globally. This will also install `npm`.
2. Navigate to `ui.frontend` in your project and run `npm ci`.

## Usage

The following npm scripts drive the frontend workflow:

- `npm start` - Starts a static webpack development live reload server for local development; it proxies requests to AEM and handles the injection of the generated webpack css/js into the page. More on this below.
- `npm run dev` - Full build of client libraries with JS optimization disabled (tree shaking, etc) and source maps enabled and CSS optimization disabled.
- `npm run prod` - Full build of client libraries build with JS optimization enabled (tree shaking, etc), source maps disabled and CSS optimization enabled. (the maven build runs this by default to generate the clientlibs.)
- `npm run format` - Formats all frontend code

## Live Reload Server and Frontend Component Development

To run the live reload server, run `npm start`, it should open a browser tab and should show your running AEM instance.

### Working with the Live Reload Server:

- You must have a running AEM isntance, on `localhost:4502`
- You must have deployed the full project at-least once: (in repo root folder, run `mvn clean install -PautoInstallSinglePAckage`)
- You must have an exisiting (or created) and AEM Page with your component authored on it. Let's assume I've created the page `/content/kpmg/en.html`
- Once you have run the dev server with `npm start`, it will open the devserver at port `8081`. (it might open in a different port, if that one's blocked)
- Navigate to your AEM page while on the dev server (let's use `localhost:8081/content/kpmg/en.html` for this example)
- You should now see any CSS/JS changes you make reflecting on that page.
- Finally, once you are happy with your changes, run the full build and test your changes on the AEM instance.

### Page Policy and Brand Content

for local development (and for demo purposes), each component will have it's own page in AEM under path `/content/kpmg-style-guide/components`.

For example, the button page: /content/kpmg-style-guide/components/button` shall include all style variations of a button.

The content under `/content/kpmg-style-guide` is static, meaning what's in the repository will be deployed and changes (locally or on cloud service) will be overwritten. This guarantees clean pages on every deploy. It's the devlopers task to keep pages updated and work out of them.

#### Helper Scripts for keeping content up-to-date

Please follow the following steps if you want to save your changes to pages/policies in the git repo

1. Push changes from local git repo to local AEM Instance: './helper-scripts/push-content-and-policies.sh'
2. Make your Page/Policy Changes
3. Pull changes from local AEM to local git repo: './helper-scripts/pull-content-and-policies.sh'
4. Inspect the file changes, make sure only the changes you want are there
5. Commit the pulled changes.

### General

The ui.frontend module compiles the code under the `ui.frontend/src` folder and outputs the compiled CSS and JS, and any resources beneath a folder named `ui.frontend/dist`.

- **Site** - `site.js`, `site.css` and a `resources/` folder for layout dependent images and fonts are created in a `dist/clientlib-site` folder.
- **Dependencies** - `dependencies.js` and `dependencies.css` are created in a `dist/clientlib-dependencies` folder.

### JavaScript

- **Optimization** - for production builds, all JS that is not being used or
  called is removed.

### CSS

- **Autoprefixing** - all CSS is run through a prefixer and any properties that require prefixing will automatically have those added in the CSS.
- **Optimization** - at post, all CSS is run through an optimizer (cssnano) which normalizes it according to the following default rules:
  - Reduces CSS calc expression wherever possible, ensuring both browser compatibility and compression.
  - Converts between equivalent length, time and angle values. Note that by default, length values are not converted.
  - Removes comments in and around rules, selectors & declarations.
  - Removes duplicated rules, at-rules and declarations. Note that this only works for exact duplicates.
  - Removes empty rules, media queries and rules with empty selectors, as they do not affect the output.
  - Merges adjacent rules by selectors and overlapping property/value pairs.
  - Ensures that only a single `@charset` is present in the CSS file and moves it to the top of the document.
  - Replaces the CSS initial keyword with the actual value, when the resulting output is smaller.
  - Compresses inline SVG definitions with SVGO.
- **Cleaning** - explicit clean task for wiping out the generated CSS, JS and Map files on demand.
- **Source Mapping** - development build only.

### Prettier Format

Prettier format validation is run as part of the maven build and will **fail** if code does not follow the prettier standard format.

If you need to run the formatter and auto-fix issues, run `npm run format`
If you need to deploy the project locally without formatting (for testing purposes): use the flag `-DskipFormat` with your maven command.

If you use VS code, use the prettier extension:
https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode
And use the `Format On Save` feature.

### Client Library Generation

The second part of the ui.frontend module build process leverages the [aem-clientlib-generator](https://www.npmjs.com/package/aem-clientlib-generator) plugin to move the compiled CSS, JS and any resources into the `ui.apps` module. The aem-clientlib-generator configuration is defined in `clientlib.config.js`. The following client libraries are generated:

- **clientlib-site** - `ui.apps/src/main/content/jcr_root/apps/<app>/clientlibs/clientlib-site`
- **clientlib-dependencies** - `ui.apps/src/main/content/jcr_root/apps/<app>/clientlibs/clientlib-dependencies`

### Page Inclusion

`clientlib-site` and `clientlib-dependencies` categories are included on pages via the Page Policy configuration as part of the default template. To view the policy, edit the **Content Page Template** > **Page Information** > **Page Policy**.

The final inclusion of client libraries on the sites page is as follows:

```html
<html>
  <head>
    <link rel="stylesheet" href="clientlib-base.css" type="text/css" />
    <script type="text/javascript" src="clientlib-dependencies.js"></script>
    <link rel="stylesheet" href="clientlib-dependencies.css" type="text/css" />
    <link rel="stylesheet" href="clientlib-site.css" type="text/css" />
  </head>
  <body>
    ....
    <script type="text/javascript" src="clientlib-site.js"></script>
    <script type="text/javascript" src="clientlib-base.js"></script>
  </body>
</html>
```

The above inclusion can of course be modified by updating the Page Policy and/or modifying the categories and embed properties of respective client libraries.

### Static Webpack Development Server

> NOTE: while this is still available, we recommend the Live Reload Server discussed above for frontend development.

Included in the ui.frontend module is a [webpack-dev-server](https://github.com/webpack/webpack-dev-server) that provides live reloading for rapid front-end development outside of AEM. The setup leverages the [html-webpack-plugin](https://github.com/jantimon/html-webpack-plugin) to automatically inject CSS and JS compiled from the ui.frontend module into a static HTML template.

#### Important files

- `ui.frontend/webpack.dev.server.js` - This contains the configuration for the webpack-dev-serve and uses AEM pages for HTML (via proxy). It allows live reload on pages that include the generated clientlibs. By default, it proxies to `localhost:4502`. See the file for more detail. See Live reload Server documentation above.

- `ui.frontend/webpack.dev.js` - (_not recommended to use for general development, but remians included for specific use-cases_)This contains the configuration for the webpack-dev-serve and points to the html template to use. It also contains a proxy configuration to an AEM instance running on `localhost:4502`.
  - `ui.frontend/src/main/webpack/static/index.html`
    This is the static HTML that the server will run against. This allows a developer to make CSS/JS changes and see them immediately reflected in the markup. It is assumed that the markup placed in this file accurately reflects generated markup by AEM components. Note\* that markup in this file does **not** get automatically synced with AEM component markup. This file also contains references to client libraries stored in AEM, like Core Component CSS and Responsive Grid CSS. The webpack development server is set up to proxy these CSS/JS includes from a local running AEM instance based on the configuration found in `ui.frontend/webpack.dev.js`.
