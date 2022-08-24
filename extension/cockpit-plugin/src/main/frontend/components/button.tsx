import React from "react";

type ButtonProps = {
  label: string;
  icon: string;
  onClick: () => void;
};

export const Button = ({ label, icon, onClick }: ButtonProps) => (
  <button className="btn btn-default action-button" title={label} onClick={onClick}>
    {icon ? (<span className={`glyphicon glyphicon-${icon}`} />) : null}
    <span className="sr-only">{label}</span>
  </button>
);
