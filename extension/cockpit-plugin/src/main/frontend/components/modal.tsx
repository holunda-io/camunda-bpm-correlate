import * as Dialog from '@radix-ui/react-dialog';
import React, { PropsWithChildren } from "react";

type ModalProps = {
  isOpen: boolean;
};

export const Modal = ({ children, isOpen }: PropsWithChildren<ModalProps>) => {
  return (
    <Dialog.Root open={isOpen}>
      <Dialog.Portal>
        <Dialog.Overlay className='modal-backdrop fade in'>
        </Dialog.Overlay>
        <Dialog.Content className='modal' style={{ display: 'block' }}>
          <article className='modal-dialog'>
            <div className='modal-content'>
              {children}
            </div>
          </article>
        </Dialog.Content>
      </Dialog.Portal>
    </Dialog.Root>
  );
};

export const ModalHeader = ({ children }: PropsWithChildren<unknown>) => (
  <header className='modal-header'>{children}</header>
);

type ModalTitleProps = {
  as?: 'h1' | 'h2' | 'h3' | 'h4' | 'h5' | 'h6';
}

export const ModalTitle = ({ children, as: Heading = 'h2' }: PropsWithChildren<ModalTitleProps>) => (
  <Heading className='modal-title h4'>{children}</Heading>
);

export const ModalBody = ({ children }: PropsWithChildren<unknown>) => (
  <div className='modal-body'>{children}</div>
);

export const ModalFooter = ({ children }: PropsWithChildren<unknown>) => (
  <footer className='modal-footer'>{children}</footer>
);
