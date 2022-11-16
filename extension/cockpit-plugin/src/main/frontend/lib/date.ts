import { format } from "date-fns";
import { isEmpty } from "lodash-es";

export type LocalDateTimeString = string;

export const formatDate = (dateString: string | null) => dateString ?
  new Date(dateString).toLocaleDateString(undefined, { day: '2-digit', month: '2-digit', year: 'numeric' }) :
  null;

export const formatTime = (dateString: string | null) => dateString ?
  new Date(dateString).toLocaleTimeString(undefined, { hour: '2-digit', minute: '2-digit', second: '2-digit' }) :
  null;

export const formatLocalIsoDateTime = (dateString: string | null) => dateString ?
  format(new Date(dateString), "yyyy-MM-dd'T'HH:mm") :
  null;

export const formatUtcIsoDateTime = (dateString: string | null) => !isEmpty(dateString) ?
  new Date(dateString as string).toISOString() :
  null;
