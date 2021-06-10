import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApprovalRule, ApprovalRule } from '../approval-rule.model';
import { ApprovalRuleService } from '../service/approval-rule.service';

@Injectable({ providedIn: 'root' })
export class ApprovalRuleRoutingResolveService implements Resolve<IApprovalRule> {
  constructor(protected service: ApprovalRuleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApprovalRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((approvalRule: HttpResponse<ApprovalRule>) => {
          if (approvalRule.body) {
            return of(approvalRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ApprovalRule());
  }
}
