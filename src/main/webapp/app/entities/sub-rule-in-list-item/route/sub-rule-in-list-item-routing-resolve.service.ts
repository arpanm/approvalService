import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubRuleInListItem, SubRuleInListItem } from '../sub-rule-in-list-item.model';
import { SubRuleInListItemService } from '../service/sub-rule-in-list-item.service';

@Injectable({ providedIn: 'root' })
export class SubRuleInListItemRoutingResolveService implements Resolve<ISubRuleInListItem> {
  constructor(protected service: SubRuleInListItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISubRuleInListItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((subRuleInListItem: HttpResponse<SubRuleInListItem>) => {
          if (subRuleInListItem.body) {
            return of(subRuleInListItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SubRuleInListItem());
  }
}
