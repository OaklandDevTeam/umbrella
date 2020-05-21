class Logo extends HTMLElement { //custom elements must extend HTMLElement
  constructor() {
    super();

    var shadow = this.attachShadow({ mode: 'open' }); // add a shadow dom (this is where the custom element's html goes)
    shadow.innerHTML = `
    <link rel="stylesheet" type="text/css" href="u-components.css">
    <div class="u-logo-bg shadowed mildly-responsive">
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
    <div class="u-button shadowed responsive">
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

class TextField extends HTMLElement { //custom elements must extend HTMLElement

  constructor() {
    super();

    var shadow = this.attachShadow({ mode: 'open' }); // add a shadow dom (this is where the custom element's html goes)
    shadow.innerHTML = `
    <link rel="stylesheet" type="text/css" href="u-components.css">
    <input type="text" id="t-field" class="u-text-field shadowed mildly-responsive">
    `
  }

  connectedCallback() {
    this.shadowRoot.getElementById("t-field").setAttribute("placeholder", this.getAttribute("placeholder"));
    this.shadowRoot.getElementById("t-field").setAttribute("type", this.getAttribute("type"));
  }

  get value() {
    return this.shadowRoot.getElementById("t-field").value;
  }
}

window.customElements.define('u-text-field', TextField); //custom elements must be registered in this way, note the u-(element name) naming scheme.

class Card extends HTMLElement { //custom elements must extend HTMLElement

  constructor() {
    super();
  }

  connectedCallback() {
    this.setAttribute('class', 'shadowed u-card slightly-responsive');
  }
}

window.customElements.define("u-card", Card);

class Label extends HTMLElement { //custom elements must extend HTMLElement
  constructor() {
    super();

    var shadow = this.attachShadow({ mode: 'open' }); // add a shadow dom (this is where the custom element's html goes)
    shadow.innerHTML = `
    <link rel="stylesheet" type="text/css" href="u-components.css">
    <p class="u-label">
    <slot></slot>
    </p>
    `
  }
}

window.customElements.define('u-label', Label); //custom elements must be registered in this way, note the u-(element name) naming scheme.

class Checkbox extends HTMLElement { //custom elements must extend HTMLElement
  constructor() {
    super();

    this.value = false;

    var shadow = this.attachShadow({ mode: 'open' }); // add a shadow dom (this is where the custom element's html goes)
    shadow.innerHTML = `
    <link rel="stylesheet" type="text/css" href="u-components.css">
    <div class="u-checkbox-container">
      <slot class="u-checkbox-label"></slot>
      <div type="checkbox" id="c-box" class="u-checkbox responsive shadowed">
        <img class="u-check" hidden="true" id="c-mark" src = "images/check.svg"></img>
      </div>
    </div>
    `

    this.onclick = () => {
      this.value = !this.value;
      if (this.value) this.shadowRoot.getElementById("c-mark").removeAttribute("hidden");
      else this.shadowRoot.getElementById("c-mark").setAttribute("hidden", "true");
    }
  }

}

window.customElements.define('u-checkbox', Checkbox); //custom elements must be registered in this way, note the u-(element name) naming scheme.