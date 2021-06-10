import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubRule, SubRule } from '../sub-rule.model';
import { SubRuleService } from '../service/sub-rule.service';

@Injectable({ providedIn: 'root' })
export class SubRuleRoutingResolveService implements Resolve<ISubRule> {
  constructor(protected service: SubRuleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISubRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((subRule: HttpResponse<SubRule>) => {
          if (subRule.body) {
            return of(subRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SubRule());
  }
}
