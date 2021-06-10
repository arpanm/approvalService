import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApprovalRequest, ApprovalRequest } from '../approval-request.model';
import { ApprovalRequestService } from '../service/approval-request.service';

@Injectable({ providedIn: 'root' })
export class ApprovalRequestRoutingResolveService implements Resolve<IApprovalRequest> {
  constructor(protected service: ApprovalRequestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApprovalRequest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((approvalRequest: HttpResponse<ApprovalRequest>) => {
          if (approvalRequest.body) {
            return of(approvalRequest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ApprovalRequest());
  }
}
