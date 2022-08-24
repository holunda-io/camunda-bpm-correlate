import React from "react";
import { formatDate, formatTime, LocalDateTimeString } from "../lib/date";

type DateTimeProps = {
  value: LocalDateTimeString | null;
};

export const DateTime = ({ value }: DateTimeProps) => value ? (
  <time dateTime={value}>{formatDate(value)} {formatTime(value)}</time>
) : null;
