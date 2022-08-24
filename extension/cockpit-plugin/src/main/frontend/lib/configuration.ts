import { useCallback, useEffect, useState } from "react";

export type Configuration = {
  maxRetries: number;
};

export function useConfiguration(camundaRestPrefix: string) {
  const [configuration, setConfiguration] = useState<Configuration | null>(null);

  const loadConfiguration = useCallback(async () => {
    setConfiguration(await fetchConfiguration(camundaRestPrefix));
  }, [camundaRestPrefix]);

  useEffect(() => {
    loadConfiguration();
  }, []);

  return {
    configuration,
    reload: loadConfiguration
  };
}

async function fetchConfiguration(camundaRestPrefix: string): Promise<Configuration | null> {
  try {
    const response = await fetch(`${camundaRestPrefix}/configuration`);
    return response.json();
  } catch (error) {
    console.error(`Fetching configuration failed`, error);
    return null;
  }
}
