class Logo extends HTMLElement { //custom elements must extend HTMLElement
  constructor() {
    super();

    var shadow = this.attachShadow({ mode: 'open' }); // add a shadow dom (this is where the custom element's html goes)
    shadow.innerHTML = `
    <link rel="stylesheet" type="text/css" href="u-components.css">
    <div class="u-logo-bg mildly-responsive">
      <img src="images/umbrella-logo.png" class="u-logo"/>
    </div>
    `
  }
}

window.customElements.define('u-logo', Logo); //custom elements must be registered in this way, note the u-(element name) naming scheme.

class Button extends HTMLElement { //custom elements must extend HTMLElement
  constructor() {
    super();

    var shadow = this.attachShadow({ mode: 'open' }); // add a shadow dom (this is where the custom element's html goes)
    shadow.innerHTML = `
    <link rel="stylesheet" type="text/css" href="u-components.css">
    <div class="u-button responsive">
      <p class="u-button-label">
        <slot></slot>
      </p>
    </div>
    `
  }

}

window.customElements.define('u-button', Button); //custom elements must be registered in this way, note the u-(element name) naming scheme.

class FlatButton extends HTMLElement { //custom elements must extend HTMLElement
  constructor() {
    super();

    var shadow = this.attachShadow({ mode: 'open' }); // add a shadow dom (this is where the custom element's html goes)
    shadow.innerHTML = `
    <link rel="stylesheet" type="text/css" href="u-components.css">
    <div class="u-flat-button responsive">
      <p class="u-flat-button-label">
        <slot></slot>
      </p>
    </div>
    `
  }

}

window.customElements.define('u-flat-button', FlatButton); //custom elements must be registered in this way, note the u-(element name) naming scheme.