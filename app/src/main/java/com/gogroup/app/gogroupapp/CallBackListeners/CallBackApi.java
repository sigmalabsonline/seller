/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gogroup.app.gogroupapp.CallBackListeners;

import retrofit2.Response;


public interface CallBackApi<T> {

  void onSuccess(Response<T> response);

  /**
   * Invoked when a network exception occurred talking to the server or when an unexpected
   * exception occurred creating the request or processing the response.
   */
  void onFailure(String message);

  void onRetryYes();
  void onRetryNo();

}
