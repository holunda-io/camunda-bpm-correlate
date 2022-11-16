import { isNull } from "lodash-es";
import { useEffect, useState } from "react";

export const useQueryParam = (name: string) => {
  const [value, setValue] = useState(new URLSearchParams(window.location.search).get(name));

  useEffect(() => {
    const url = new URL(window.location.href);
    if (isNull(value)) {
      url.searchParams.delete(name);
    } else {
      url.searchParams.set(name, value)
    }
    window.history.pushState(null, document.title, url);
  }, [name, value]);

  return [value, setValue] as const;
}
