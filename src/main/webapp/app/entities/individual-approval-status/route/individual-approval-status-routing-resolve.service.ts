import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIndividualApprovalStatus, IndividualApprovalStatus } from '../individual-approval-status.model';
import { IndividualApprovalStatusService } from '../service/individual-approval-status.service';

@Injectable({ providedIn: 'root' })
export class IndividualApprovalStatusRoutingResolveService implements Resolve<IIndividualApprovalStatus> {
  constructor(protected service: IndividualApprovalStatusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIndividualApprovalStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((individualApprovalStatus: HttpResponse<IndividualApprovalStatus>) => {
          if (individualApprovalStatus.body) {
            return of(individualApprovalStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IndividualApprovalStatus());
  }
}
