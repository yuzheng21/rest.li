/*
   Copyright (c) 2012 LinkedIn Corp.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.linkedin.r2.filter;

import com.linkedin.r2.filter.message.rest.RestFilter;
import com.linkedin.r2.message.RequestContext;
import com.linkedin.r2.message.timing.TimingContextUtil;
import com.linkedin.r2.message.timing.TimingKey;
import com.linkedin.r2.message.rest.RestRequest;
import com.linkedin.r2.message.rest.RestResponse;
import java.util.Map;


/**
 * A decorated {@link RestFilter} that marks the beginning of timing record of the filter.
 *
 * @author Xialin Zhu
 */
/* package private */ class TimedRestFilter implements RestFilter
{
  protected static final String ON_REQUEST_SUFFIX = "onRequest";
  protected static final String ON_RESPONSE_SUFFIX = "onResponse";
  protected static final String ON_ERROR_SUFFIX = "onError";

  private final RestFilter _restFilter;
  private final TimingKey _onRequestTimingKey;
  private final TimingKey _onResponseTimingKey;
  private final TimingKey _onErrorTimingKey;

  public TimedRestFilter(RestFilter restFilter)
  {
    _restFilter = restFilter;
    String timingKeyPrefix = _restFilter.getClass().getSimpleName() + "-" + hashCode() + "-";
    _onRequestTimingKey = TimingKey.registerNewKey(timingKeyPrefix + ON_REQUEST_SUFFIX);
    _onResponseTimingKey = TimingKey.registerNewKey(timingKeyPrefix + ON_RESPONSE_SUFFIX);
    _onErrorTimingKey = TimingKey.registerNewKey(timingKeyPrefix + ON_ERROR_SUFFIX);
  }

  @Override
  public void onRestRequest(RestRequest req, final RequestContext requestContext,
    Map<String, String> wireAttrs,
    final NextFilter<RestRequest, RestResponse> nextFilter)
  {
    TimingContextUtil.markTiming(requestContext, _onRequestTimingKey);
    _restFilter.onRestRequest(req, requestContext, wireAttrs, new TimedNextFilter<>(_onRequestTimingKey, nextFilter));
  }

  @Override
  public void onRestResponse(RestResponse res,
    RequestContext requestContext,
    Map<String, String> wireAttrs,
    NextFilter<RestRequest, RestResponse> nextFilter)
  {
    TimingContextUtil.markTiming(requestContext, _onResponseTimingKey);
    _restFilter.onRestResponse(res, requestContext, wireAttrs, new TimedNextFilter<>(_onResponseTimingKey, nextFilter));
  }

  @Override
  public void onRestError(Throwable ex,
    RequestContext requestContext,
    Map<String, String> wireAttrs,
    NextFilter<RestRequest, RestResponse> nextFilter)
  {
    TimingContextUtil.markTiming(requestContext, _onErrorTimingKey);
    _restFilter.onRestError(ex, requestContext, wireAttrs, new TimedNextFilter<>(_onErrorTimingKey, nextFilter));
  }
}
