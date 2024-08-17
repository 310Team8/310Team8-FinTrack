import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/password-field/src/vaadin-password-field.js';
import '@vaadin/tooltip/src/vaadin-tooltip.js';
import '@vaadin/button/src/vaadin-button.js';
import 'Frontend/generated/jar-resources/buttonFunctions.js';
import '@vaadin/text-field/src/vaadin-text-field.js';
import '@vaadin/vertical-layout/src/vaadin-vertical-layout.js';
import '@vaadin/notification/src/vaadin-notification.js';
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '21de1218f5dba69525a4b4fbd2770f1cf0b76ae10ca16c5248419f51fa00fb2e') {
    pending.push(import('./chunks/chunk-6197cf253b8ee1c7303dcaffd7adb34bc1dc02319d1a4c9fc9f286429c0a66b6.js'));
  }
  if (key === 'bafc127275354c38a5c1cd101973621f3297a2c45a9e06967ad1ea2391d51976') {
    pending.push(import('./chunks/chunk-6197cf253b8ee1c7303dcaffd7adb34bc1dc02319d1a4c9fc9f286429c0a66b6.js'));
  }
  if (key === '3f2c3d5470ea0439a026b0259bec253b38d55df53693a55c67ccaf242f3638f1') {
    pending.push(import('./chunks/chunk-6197cf253b8ee1c7303dcaffd7adb34bc1dc02319d1a4c9fc9f286429c0a66b6.js'));
  }
  if (key === '51e53b85f824bca687e62c1c31b7bc237a2f38b30c6edbb0df1e1476ded3c6cd') {
    pending.push(import('./chunks/chunk-975fb8a3e1d255fd3c42d4925f8b2a34cca3c1dacd4a2e20a64c81450711bf85.js'));
  }
  if (key === 'f0f93aa1317092fcd5d020fe56f20c47811622c2e9403a3cf8182a3bbc2fc41b') {
    pending.push(import('./chunks/chunk-4d4b57885b5f51a8906be74d09e4f7183da61cfdc27430918d9ed6056d515957.js'));
  }
  if (key === '9d4a243c0522192fa07168a2585b5037ba4ce8825837cd73a87aa394fe311f1e') {
    pending.push(import('./chunks/chunk-64e202141c971c6b19fc4782d5b4d72bee3f286a4e38da0e2a863947379bfde2.js'));
  }
  if (key === 'e1ad94fdaa855d8c8c172cf92386f442808bf60843c4c9812a4e0c9dee1d5cd0') {
    pending.push(import('./chunks/chunk-6197cf253b8ee1c7303dcaffd7adb34bc1dc02319d1a4c9fc9f286429c0a66b6.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}