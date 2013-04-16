/**
 * $Id: $
 */

package com.linkedin.d2.discovery.util;

/**
 * @author David Hoa
 * @version $Revision: $
 */

public class D2Utils
{
  /**
   * addSuffixToBaseName will mutate a base name with a suffix in a known fashion.
   *
   * @param baseName original string (can be cluster name or service name) to mutate
   * @param suffix string to append in a known fashion
   * @return new string that is a combination of baseName and suffix
   */
  public static String addSuffixToBaseName(String baseName, String suffix)
  {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append(baseName);
    if (suffix != null && !suffix.isEmpty())
    {
      strBuilder.append("-").append(suffix);
    }
    return strBuilder.toString();
  }
}
