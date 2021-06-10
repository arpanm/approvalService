import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApprovalLevelStatus, ApprovalLevelStatus } from '../approval-level-status.model';
import { ApprovalLevelStatusService } from '../service/approval-level-status.service';

@Injectable({ providedIn: 'root' })
export class ApprovalLevelStatusRoutingResolveService implements Resolve<IApprovalLevelStatus> {
  constructor(protected service: ApprovalLevelStatusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApprovalLevelStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((approvalLevelStatus: HttpResponse<ApprovalLevelStatus>) => {
          if (approvalLevelStatus.body) {
            return of(approvalLevelStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ApprovalLevelStatus());
  }
}
