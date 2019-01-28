package com.yash.microservices.feedback;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, UsernamePattern.class, Username.class, Source.class, Rating.class})
interface ValidationSequence {

}

interface Username {}
interface UsernamePattern{}
interface Source{}
interface Rating{}
