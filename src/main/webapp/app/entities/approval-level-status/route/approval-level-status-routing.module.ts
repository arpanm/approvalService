import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApprovalLevelStatusComponent } from '../list/approval-level-status.component';
import { ApprovalLevelStatusDetailComponent } from '../detail/approval-level-status-detail.component';
import { ApprovalLevelStatusUpdateComponent } from '../update/approval-level-status-update.component';
import { ApprovalLevelStatusRoutingResolveService } from './approval-level-status-routing-resolve.service';

const approvalLevelStatusRoute: Routes = [
  {
    path: '',
    component: ApprovalLevelStatusComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApprovalLevelStatusDetailComponent,
    resolve: {
      approvalLevelStatus: ApprovalLevelStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApprovalLevelStatusUpdateComponent,
    resolve: {
      approvalLevelStatus: ApprovalLevelStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApprovalLevelStatusUpdateComponent,
    resolve: {
      approvalLevelStatus: ApprovalLevelStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(approvalLevelStatusRoute)],
  exports: [RouterModule],
})
export class ApprovalLevelStatusRoutingModule {}
