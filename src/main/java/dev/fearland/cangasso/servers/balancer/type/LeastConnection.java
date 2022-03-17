package dev.fearland.cangasso.servers.balancer.type;

import dev.fearland.cangasso.servers.balancer.elements.LoadBalancerObject;
import dev.fearland.cangasso.servers.balancer.elements.NumberConnection;
import dev.fearland.cangasso.servers.balancer.BaseBalancer;

public class LeastConnection<T extends LoadBalancerObject & NumberConnection> extends BaseBalancer<T> {

  @Override
  public T next() {
    T obj = null;
    if (nextObj != null) {
      if (!nextObj.isEmpty()) {
        for (T item : nextObj) {
          if (!item.canBeSelected()) {
            continue;
          }

          if (obj == null) {
            obj = item;
            continue;
          }

          if (obj.getActualNumber() >= item.getActualNumber()) {
            obj = item;
          }
        }
      }
    }

    return obj;
  }

  @Override
  public int getTotalNumber() {
    int number = 0;
    for (T item : nextObj) {
      number += item.getActualNumber();
    }
    return number;
  }
}
