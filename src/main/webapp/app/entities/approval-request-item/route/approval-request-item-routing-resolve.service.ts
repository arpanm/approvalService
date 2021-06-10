import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApprovalRequestItem, ApprovalRequestItem } from '../approval-request-item.model';
import { ApprovalRequestItemService } from '../service/approval-request-item.service';

@Injectable({ providedIn: 'root' })
export class ApprovalRequestItemRoutingResolveService implements Resolve<IApprovalRequestItem> {
  constructor(protected service: ApprovalRequestItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApprovalRequestItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((approvalRequestItem: HttpResponse<ApprovalRequestItem>) => {
          if (approvalRequestItem.body) {
            return of(approvalRequestItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ApprovalRequestItem());
  }
}
