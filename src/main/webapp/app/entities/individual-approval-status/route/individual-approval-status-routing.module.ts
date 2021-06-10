import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IndividualApprovalStatusComponent } from '../list/individual-approval-status.component';
import { IndividualApprovalStatusDetailComponent } from '../detail/individual-approval-status-detail.component';
import { IndividualApprovalStatusUpdateComponent } from '../update/individual-approval-status-update.component';
import { IndividualApprovalStatusRoutingResolveService } from './individual-approval-status-routing-resolve.service';

const individualApprovalStatusRoute: Routes = [
  {
    path: '',
    component: IndividualApprovalStatusComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IndividualApprovalStatusDetailComponent,
    resolve: {
      individualApprovalStatus: IndividualApprovalStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IndividualApprovalStatusUpdateComponent,
    resolve: {
      individualApprovalStatus: IndividualApprovalStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IndividualApprovalStatusUpdateComponent,
    resolve: {
      individualApprovalStatus: IndividualApprovalStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(individualApprovalStatusRoute)],
  exports: [RouterModule],
})
export class IndividualApprovalStatusRoutingModule {}
