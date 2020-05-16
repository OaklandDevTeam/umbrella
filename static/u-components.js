class logo extends HTMLElement { //custom elements must extend HTMLElement
  constructor() {
    super();

    var shadow = this.attachShadow({ mode: 'open' }); // add a shadow dom (this is where the custom element's html goes)
    shadow.innerHTML = `
    <link rel="stylesheet" type="text/css" href="u-components.css">
    <div class="u-logo-bg">
      <img src="images/umbrella-logo.png" class="u-logo"/>
    </div>
    `
  }
}

window.customElements.define('u-logo', logo); //custom elements must be registered in this way, note the u-(element name) naming scheme.