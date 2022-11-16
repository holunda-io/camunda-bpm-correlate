import React from "react";
import { Message } from "../lib/message";
import { Modal, ModalBody, ModalFooter, ModalHeader, ModalTitle } from "./modal";

type StacktraceModalProps = {
  message: Message;
  onClose: () => void;
};

export const StacktraceModal = ({ message, onClose }: StacktraceModalProps) => {
  return (
    <Modal isOpen={true}>
      <ModalHeader>
        <ModalTitle>View Stacktrace</ModalTitle>
      </ModalHeader>
      <ModalBody>
        <label className='hovered'>
          <span>Copy stacktrace to clipboard</span>
          <a className="glyphicon glyphicon-copy" onClick={() => { navigator.clipboard.writeText(message?.error as string) }}></a>
        </label>
        <textarea rows={20} readOnly={true} className='form-control cam-string-variable vertical-resize'>
          {message.error}
        </textarea>
      </ModalBody>
      <ModalFooter>
        <button className={'btn btn-default'} onClick={onClose}>Close</button>
      </ModalFooter>
    </Modal>
  )
}
