import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApprovalRequestComponent } from '../list/approval-request.component';
import { ApprovalRequestDetailComponent } from '../detail/approval-request-detail.component';
import { ApprovalRequestUpdateComponent } from '../update/approval-request-update.component';
import { ApprovalRequestRoutingResolveService } from './approval-request-routing-resolve.service';

const approvalRequestRoute: Routes = [
  {
    path: '',
    component: ApprovalRequestComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApprovalRequestDetailComponent,
    resolve: {
      approvalRequest: ApprovalRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApprovalRequestUpdateComponent,
    resolve: {
      approvalRequest: ApprovalRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApprovalRequestUpdateComponent,
    resolve: {
      approvalRequest: ApprovalRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(approvalRequestRoute)],
  exports: [RouterModule],
})
export class ApprovalRequestRoutingModule {}
