import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApproverComponent } from '../list/approver.component';
import { ApproverDetailComponent } from '../detail/approver-detail.component';
import { ApproverUpdateComponent } from '../update/approver-update.component';
import { ApproverRoutingResolveService } from './approver-routing-resolve.service';

const approverRoute: Routes = [
  {
    path: '',
    component: ApproverComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApproverDetailComponent,
    resolve: {
      approver: ApproverRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApproverUpdateComponent,
    resolve: {
      approver: ApproverRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApproverUpdateComponent,
    resolve: {
      approver: ApproverRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(approverRoute)],
  exports: [RouterModule],
})
export class ApproverRoutingModule {}
