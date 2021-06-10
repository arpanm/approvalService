import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApprovalRequestItemComponent } from '../list/approval-request-item.component';
import { ApprovalRequestItemDetailComponent } from '../detail/approval-request-item-detail.component';
import { ApprovalRequestItemUpdateComponent } from '../update/approval-request-item-update.component';
import { ApprovalRequestItemRoutingResolveService } from './approval-request-item-routing-resolve.service';

const approvalRequestItemRoute: Routes = [
  {
    path: '',
    component: ApprovalRequestItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApprovalRequestItemDetailComponent,
    resolve: {
      approvalRequestItem: ApprovalRequestItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApprovalRequestItemUpdateComponent,
    resolve: {
      approvalRequestItem: ApprovalRequestItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApprovalRequestItemUpdateComponent,
    resolve: {
      approvalRequestItem: ApprovalRequestItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(approvalRequestItemRoute)],
  exports: [RouterModule],
})
export class ApprovalRequestItemRoutingModule {}
