# KPMG Style Dictionary/Design Tokens

**Version: 0.0.3**

**Last Updated on: May 16, 2023**

This folder is generated via the **[style-dictionary](https://amzn.github.io/style-dictionary/#/)** framework. We encourage you take a few minutes to familiarize yourself with how this system works.

> **Note: This folder is built via the parent package.json
> to understand what this folder generates, consult the config.json file.**

---

## How it works

> **Note: When running the _npm run start_ command inside of the _ui.frontend_ folder, you will see that it will generate a _\_tokens.scss_ file that can be found here:**

**ui.frontend/src/main/webpack/site/\_tokens.scss**

In this file you will see all the available design tokens represented as **SASS** variables.

All of the design token files are in the tokens folder found here:

**ui.frontend/style-dictionary/tokens/**

Inside this folder you will find that token files are separated into two folders representing the different token categories:

**ui.frontend/style-dictionary/tokens/global/**

and

**ui.frontend/style-dictionary/tokens/alias/**

For more information on the differences between **Global** and **Alias** tokens, please see **Design Tokens Categories** section.

> **Note: When looking the token files in either folder you will see that the format of the file is _.json_.**

Let's look at an abbreviated version of the **Global** token file found at:

**ui.frontend/style-dictionary/tokens/global/color/global.color.brand.json**

Here is an abbreviated example of this file:

**ex.**

```json
{
  "global": {
    "color": {
      "brand": {
        "spectrum-blue": {
          "value": "#0C233C"
        },
        "kpmg-blue": {
          "value": "#00338D",
          "comment": "Refer to Brand book as Dark Blue"
        },
        "dark-pink": {
          "value": "#AB0D82"
        },
        "pink": {
          "value": "#FD349C"
        },
        "light-pink": {
          "value": "#FFA3DA"
        },
        "dark-green": {
          "value": "#098E7E"
        },
        "green": {
          "value": "#00C0AE"
        },
        "light-green": {
          "value": "#63EBDA"
        },
        "white": {
          "value": "#FFFFFF"
        }
      }
    }
  }
}
```

In this case what gets generated is a **SASS** variable found here:

**ui.frontend/src/main/webpack/site/\_tokens.scss**

In the case of the **global.color.brand.kpmg-blue** entry you will find the following generated design token/variable in the **\_tokens.scss** file:

**ex.**

```sass
$global-color-brand-kpmg-blue: #00338D; // Refer to Brand book as Dark Blue
```

As you can see variable name has been flattened while still retaining the structure of the **JSON** file with the **`.`** between each key replaced with a **`-`**.

Too get an idea of how **Alias** token files are set up, let's look at an abbreviated version of the **Alias** token file found at:

**ui.frontend/style-dictionary/tokens/alias/size/alias.size.margin.json**

**ex.**

```json
{
  "alias": {
    "margin": {
      "xxx-small": {
        "value": "{global.size.0125}",
        "comment": "2px"
      },
      "xx-small": {
        "value": "{global.size.025}",
        "comment": "4px"
      },
      "x-small": {
        "value": "{global.size.05}",
        "comment": "8px"
      },
      "small": {
        "value": "{global.size.075}",
        "comment": "12px"
      },
      "base": {
        "value": "{global.size.1}",
        "comment": "16px"
      },
      "medium": {
        "value": "{global.size.15}",
        "comment": "24px"
      },
      "large": {
        "value": "{global.size.2}",
        "comment": "32px"
      },
      "x-large": {
        "value": "{global.size.25}",
        "comment": "40px"
      },
      "xx-large": {
        "value": "{global.size.3}",
        "comment": "48px"
      },
      "xxx-large": {
        "value": "{global.size.4}",
        "comment": "64px"
      },
      "4x-large": {
        "value": "{global.size.5}",
        "comment": "80px"
      },
      "5x-large": {
        "value": "{global.size.6}",
        "comment": "96px"
      },
      "10x-large": {
        "value": "{global.size.10}",
        "comment": "160px"
      }
    }
  }
}
```

Notice in this example the values for these tokens are being referenced from the **Global** token file found here:

**ui.frontend/style-dictionary/tokens/global/size/global.size.base.json**

> **Important ! : Please note that the sizes found in global.size.base.json are based on the [Carbon Spacing System](https://carbondesignsystem.com/guidelines/spacing/overview/) This size scale has been mapped to the margin/padding _Alias_ tokens. This is a standardized scale that can be found in many design systems and should be used for spacing.**

Note that the path in **Alias** token file is identical to the **JSON** structure in this particular **Global** token file.

For a more detailed explanation of why we are doing this, please see the next section.

> **Important ! : _Alias_ design tokens always inherit their values from _Global_ design tokens. Do not hard code any standalone values in the _Alias_ token files.**

> **Note: This package is currently set up to only output _SASS_ web code, but has the capability of outputting other formats for iOS and Android if needed.**

---

## Design Tokens Categories

The design tokens in our system are divided into two categories:

**`Global => Alias`**

### Global Tokens

**Global** tokens are the primitive values in our design language, represented by context-agnostic names. Our color palette, animation, typography, and dimension values are all recorded as **Global** tokens. They can be found in:

**ui.frontend/style-dictionary/tokens/global/**

**ex.**

```scss
$global-color-brand-spectrum-blue: #0c233c;
$global-color-brand-kpmg-blue: #00338d; // Refer to Brand book as Dark Blue
$global-color-brand-cobalt-blue: #1e49e2;
$global-size-1: 1rem;
```

### Alias Tokens

**Alias** tokens relate to a specific context or abstraction. Aliases help communicate the intended purpose of a token, and are effective when a value with a single intent will appear in multiple places. They can be found in:

**ui.frontend/style-dictionary/tokens/alias/**

> **Note: _Alias_ design tokens always inherit their values from _Global_ token values. Please feel free to look at the _Global_ token files and see the connection to the _Alias_ token files.**

**ex.**

```scss
$alias-fonts-heading-one-font-family: OpenSans_Condensed--Bold;
$alias-font-size-base: 1rem;
$alias-margin-10x-large: 10rem;
$alias-padding-5x-large: 6rem;
$alias-color-button-background-color-primary-enabled: #1e49e2;
```

> **Note: Remember the values you see in the above example are inherited from _Global_ design tokens, but when generated they will have their hard coded values taken from the _Global_ token.**

For more information on the difference between **Global** and **Alias** tokens please read **[this article](https://uxdesign.cc/supercharge-your-design-system-with-design-tokens-55044fa29142)**.

### Component Specific Tokens

> **Note: You may hear or read about another level of tokens referred to as _Component-specific_ tokens, in our case we are not utilizing them the way most design systems do, you will find these tokens/variables located inside the individual component style files found here:**

**ui.frontend/src/main/webpack/components/**

Here is an example of how they are used, this is an abbreviated sample from the **standard teaser** component:

**ex.**

```sass
.standardteaser {
  $color-main: $global-color-grayscale-black; // #333333
  $dark-theme-text: $global-color-brand-white; // #FFFFFF
  $margin-base: $alias-margin-base; // 16px
  $margin-medium: $alias-margin-medium; // 24px
  $margin-large: $alias-margin-large; // 32px
  $margin-x-large: $alias-margin-x-large; // 40px
  $margin-xx-large: $alias-margin-xx-large; // 48px
  $font-size-medium: $alias-font-size-medium; // 24px
  $font-size-large: $alias-font-size-large; // 32px
  $image-width: 480px;
  $image-height: 600px;
  $mobile-font-size-pretitle: 20px;
  $mobile-line-height-pretitle: 28px;

  width: 100%;

  .cmp-teaser {
    @include full-width;
    color: $color-main;
    padding: $alias-padding-medium;
    padding-bottom: 0;
    display: flex;
    width: 100%;
    flex-direction: column;
    height: unset;
    max-height: unset;
    word-break: break-word;

    .cmp-teaser__content {
      display: grid;
    }

    .cmp-teaser__pretitle {
      order: 2;
      font-size: $mobile-font-size-pretitle;
      line-height: $mobile-line-height-pretitle;
      margin-top: 0;
    }

    .cmp-teaser__title {
      order: 1;
      margin: $margin-medium 0 $margin-x-large 0;
    }

    .cmp-teaser__description {
      order: 3;
    }

    .cmp-teaser__action-container {
      order: 4;
      margin-top: $margin-base;
      margin-bottom: $margin-x-large;
    }

    .cmp-teaser__image {
      margin: 0 auto;
      @include in-front-of-bleed;
      line-height: 0;

      img {
        max-width: $image-width;
        height: auto;
      }
    }
  }
}

```

In this file the **Component-specific** tokens are at the top of the file:

**ex.**

```scss
$color-main: $global-color-grayscale-black; // #333333
$dark-theme-text: $global-color-brand-white; // #FFFFFF
$margin-base: $alias-margin-base; // 16px
$margin-medium: $alias-margin-medium; // 24px
$margin-large: $alias-margin-large; // 32px
$margin-x-large: $alias-margin-x-large; // 40px
$margin-xx-large: $alias-margin-xx-large; // 48px
$font-size-medium: $alias-font-size-medium; // 24px
$font-size-large: $alias-font-size-large; // 32px
$image-width: 480px;
$image-height: 600px;
$mobile-font-size-pretitle: 20px;
$mobile-line-height-pretitle: 28px;
```

> **Important ! : Looking at these _Component-specific_ tokens/variables, you will notice that they are wrapped in the _.standardteaser_ class, this is to ensure the locally used token/variable is not exposed on a _Global_ level which can interfere with similarly named tokens used in other components as well as the site in general.**
>
> **You will also notice that some tokens/variables point to _Global_ level tokens, others refer to _Alias_ level tokens, and some are just hard-coded values that don't refer to a design token at all. While using _Global_ tokens is currently accepted, we strongly suggest avoiding referencing them in the component stylesheet and instead try to use _Alias_ level tokens if possible.**

---

## Guidelines for token creation

**1. Avoid using _Global_ tokens if an _Alias_ level token will work better.**

This is to avoid using a token that is too specific in its naming:

**ex.**

Using

**`color: $global-color-brand-kpmg-blue`**

instead of

**`color: $alias-color-heading-one`**

> **Note: The first token is too specific in its name and will cause more refactoring if a design/theme/brand change happens in the future**.

> **Note: You may find instances where just using a _Global_ token makes more sense than creating an _Alias_ level token. Feel free to go with a _Global_ token with the understanding that any change to that token in the future may break the styling of your component.**

**2. Try to write your token names (at any level) in a way that you as well as other developers can understand. Developers should be able to determine the purpose of the token quickly without delving further into documentation based in the token's name. The Style Dictionary file format will help us with this.**
