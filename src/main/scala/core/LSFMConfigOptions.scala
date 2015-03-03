package core

/**
 * Simple container class that is used to store the options that are defined in LivestreamerFM's config file.
 * Created by gmgilmore on 3/2/15.
 */


case class LSFMConfigOptions(playerLocation: String, ipAndPort: String,
                             delay: String, livestreamerConfigLocation: String)

