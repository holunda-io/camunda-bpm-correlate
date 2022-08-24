export type Plugin = {
  id: string;
  pluginPoint: 'cockpit.route' | 'cockpit.navigation' | 'cockpit.metrics';
  priority: number;
  properties?: {
    path?: string;
  };
  render: (node: HTMLElement, { api }: { api: API }) => void;
  unmount?: () => void;
};

export type API = {
  cockpitApi: string;
  engine: string;
}
