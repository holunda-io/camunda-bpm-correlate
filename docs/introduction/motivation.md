Correlation is about targeting a running workflow (for example running inside the Camunda Platform 7) 
containing the state update by an external system. Inside the Camunda Platform it is important that the 
message subscription is present at the time of correlation, otherwise the correlation is mismatched.

If you are building a distributed system using the Camunda Platform 7 as a part of it, you should not 
make assumptions or assertions regarding the speed of processing of components, message ordering, 
message delivery or timings. To make sure that the correlation is not dependent on all those assumptions,
the usage of inbox pattern to store the message locally and then deliver it timely on schedule is a good 
practise.
