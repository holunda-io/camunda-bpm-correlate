The library provides a core that is responsible for accepting the message, storing it into persistence storage
and processing it scheduled. If any errors occur during the correlation, these are handled by one of the
pre-configured error strategies, like retry, ignore or drop... 

In addition, there are a set of several ingress adapters to support different communication technologies.
