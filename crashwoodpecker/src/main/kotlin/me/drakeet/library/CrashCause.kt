package me.drakeet.library

import java.io.Serializable
import java.util.ArrayList

/**
 * Created by drakeet on 8/31/15.
 */
public class CrashCause: Serializable {
    public var stackTraceElements : Array<StackTraceElement>? = null
}