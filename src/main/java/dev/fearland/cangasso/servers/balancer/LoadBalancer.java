package dev.fearland.cangasso.servers.balancer;

import dev.fearland.cangasso.servers.balancer.elements.LoadBalancerObject;

public interface LoadBalancer<T extends LoadBalancerObject> {

  public T next();
}
