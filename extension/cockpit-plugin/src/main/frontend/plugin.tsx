import React from "react";
import ReactDOM from "react-dom";
import CorrelateMessagesView from "./correlate-messages-view";
import { Plugin } from './lib/camunda-plugin';

let container: HTMLElement;

const correlateView: Plugin = {
  id: 'correlate-cockpit-plugin-route',
  pluginPoint: 'cockpit.route',
  priority: 4,
  properties: {
    path: '/correlation'
  },
  render: (node: HTMLElement, { api }) => {
    container = node;
    const urlPrefix = `${api.cockpitApi}/plugin/correlate-cockpit-plugin/${api.engine}`;
    ReactDOM.render(
      <CorrelateMessagesView camundaRestPrefix={urlPrefix} />,
      container
    );
  },
  unmount: () => {
    ReactDOM.unmountComponentAtNode(container);
  }
};

const navigation: Plugin = {
  id: 'correlate-cockpit-plugin-navigation',
  pluginPoint: 'cockpit.navigation',
  priority: 4,
  properties: {
    path: '/correlation'
  },
  render: (container) => {
    container.innerHTML = '<a href="#/correlation">Correlation</a>';
  }
};

// TODO: add metric reporting eventually
/*
const metrics: Plugin = {
  id: 'correlate-cockpit-plugin-metrics',
  pluginPoint: 'cockpit.metrics',
  priority: 4,
  render: (container) => {
    container.innerHTML = '<div>Metrics</div>';
  }
};
*/

export default [correlateView, navigation];
